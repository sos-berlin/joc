package com.sos.joc.joe.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.ClusterMemberHandler;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.DeployJoeAudit;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.joe.resource.IDeployResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.DeployAnswer;
import com.sos.joc.model.joe.common.DeployMessage;
import com.sos.joc.model.joe.common.FilterDeploy;

@Path("joe")
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    private static final String API_CALL = "./joe/deploy";

    @Override
    public JOCDefaultResponse deploy(final String accessToken, final FilterDeploy body) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isDeploy();
            boolean permission2 = Helper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("folder", body.getFolder());
            if (body.getObjectType() != null && !body.getObjectType().value().isEmpty() && !Helper.CLASS_MAPPING.containsKey(body.getObjectType()
                    .value())) {
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
            }

            String folder = normalizeFolder(body.getFolder());
            if (!folderPermissions.isPermittedForFolder(folder)) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(sosHibernateSession);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();

            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            if (body.getObjectName() != null) {
                filterJoeObjects.setPath(normalizePath(folder) + "/" + body.getObjectName());
            } else {
                filterJoeObjects.setPath(normalizeFolder(folder));
            }
            if (JobSchedulerObjectType.ORDER == body.getObjectType() || JobSchedulerObjectType.JOBCHAIN == body.getObjectType()) {
                filterJoeObjects.setObjectTypes(body.getObjectType().value(), JobSchedulerObjectType.NODEPARAMS.value());
            } else {
                filterJoeObjects.setObjectType(body.getObjectType());
            }
            filterJoeObjects.setOrderCriteria("created");
            if ((body.getObjectName() == null || body.getObjectName().isEmpty()) && (body.getRecursive() != null && body.getRecursive())) {
                filterJoeObjects.setRecursive();
            }

            List<DBItemJoeObject> listOfJoeObjects = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);
            JOCHotFolder jocHotFolder = new JOCHotFolder(this);

            DeployAnswer deployAnswer = new DeployAnswer();
            deployAnswer.setMessages(new ArrayList<DeployMessage>());
            deployAnswer.setFolder(body.getFolder());
            deployAnswer.setJobschedulerId(body.getJobschedulerId());
            deployAnswer.setObjectName(body.getObjectName());
            deployAnswer.setObjectType(body.getObjectType());
            deployAnswer.setRecursive(body.getRecursive());
            InventoryFilesDBLayer inventoryFilesDBLayer = new InventoryFilesDBLayer(sosHibernateSession);
            
            boolean objectsHaveBeenDeployed = false;

            if (listOfJoeObjects != null) {
                
                String[] objTypes = {"NODEPARAMS", "PROCESSCLASS", "AGENTCLUSTER", "LOCK", "MONITOR", "SCHEDULE", "JOB", "JOBCHAIN", "ORDER"};
                Map<String, Set<DBItemJoeObject>> groupedJoeObjects = listOfJoeObjects.stream().collect(Collectors.groupingBy(
                        DBItemJoeObject::getObjectType, Collectors.toSet()));
                for (String objType : objTypes) {
                    if (groupedJoeObjects.containsKey(objType)) {
                        for (DBItemJoeObject joeObject : groupedJoeObjects.get(objType)) {

                            DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                            logAuditMessage(deployJoeAudit);

                            if (!folderPermissions.isPermittedForFolder(getParent(joeObject.getPath()))) {
                                deployAnswer.getMessages().add(getAccessDeniedMessage(joeObject.getPath()));
                                continue;
                            }

                            String extension = Helper.getFileExtension(JobSchedulerObjectType.fromValue(joeObject.getObjectType()));
                            ClusterMemberHandler clusterMemberHandler = new ClusterMemberHandler(dbItemInventoryInstance, joeObject.getPath() + extension, API_CALL);

                            objectsHaveBeenDeployed = true;
                            switch (joeObject.getOperation().toLowerCase()) {
                            case "store":
                                String xmlContent = XmlSerializer.serializeToStringWithHeader(joeObject.getConfiguration(), joeObject.getObjectType());
                                jocHotFolder.putFile(joeObject.getPath() + extension, xmlContent);
                                clusterMemberHandler.updateAtOtherClusterMembers(xmlContent);
                                break;
                            case "delete":
                                jocHotFolder.deleteFile(joeObject.getPath() + extension);
                                clusterMemberHandler.deleteAtOtherClusterMembers();
                                break;
                            default:
                                break;
                            }

                            storeAuditLogEntry(deployJoeAudit);
                            dbLayerJoeObjects.delete(joeObject);
                        }
                    }
                }

                if (objectsHaveBeenDeployed) {
                    java.lang.Thread.sleep(2000); // waiting for inventory
                }

                if (groupedJoeObjects.containsKey("FOLDER")) {
                    for (DBItemJoeObject joeObject : groupedJoeObjects.get("FOLDER")) {

                        boolean deleteEntry = false;
                        DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                        logAuditMessage(deployJoeAudit);

                        if (!folderPermissions.isPermittedForFolder(joeObject.getPath())) {
                            deployAnswer.getMessages().add(getAccessDeniedMessage(joeObject.getPath()));
                            continue;
                        }

                        switch (joeObject.getOperation().toLowerCase()) {
                        case "store":
                            deleteEntry = !inventoryFilesDBLayer.isEmptyFolder(this.dbItemInventoryInstance.getId(), joeObject.getPath());
                            jocHotFolder.putFolder(normalizeFolder(joeObject.getPath()));
                            break;
                        case "delete":
                            deleteEntry = true;
                            jocHotFolder.deleteFolder(normalizeFolder(joeObject.getPath()));
                            break;

                        default:
                            break;
                        }

                        storeAuditLogEntry(deployJoeAudit);

                        if (deleteEntry) {
                            dbLayerJoeObjects.delete(joeObject);
                        }
                    }
                }
            }
            Globals.commit(sosHibernateSession);

            return JOCDefaultResponse.responseStatus200(deployAnswer);

        } catch (

        JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private DeployMessage getAccessDeniedMessage(String path) {
        DeployMessage deployMessage = new DeployMessage();
        deployMessage.setMessage("Access denied");
        deployMessage.setPermissionDeniedFor(path);
        return deployMessage;
    }

}
