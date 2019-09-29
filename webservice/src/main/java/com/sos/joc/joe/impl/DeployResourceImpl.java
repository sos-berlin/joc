package com.sos.joc.joe.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.audit.DeployJoeAudit;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IDeployResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;

@Path("joe")
public class DeployResourceImpl extends JOCResourceImpl implements IDeployResource {

    private static final String API_CALL = "./joe/deploy";

    @Override
    public JOCDefaultResponse deploy(final String accessToken, final Filter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            checkRequiredParameter("objectType", body.getObjectType());

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

            checkRequiredParameter("path", body.getPath());

            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;
            if (!isDirectory && !Helper.CLASS_MAPPING.containsKey(body.getObjectType().value())) {
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
            }

            String path = "";
            if (isDirectory) {
                if (!folderPermissions.isPermittedForFolder(path)) {
                    return accessDeniedResponse();
                }
                path = normalizeFolder(body.getPath());
            } else {
                if (!folderPermissions.isPermittedForFolder(getParent(path))) {
                    return accessDeniedResponse();
                }
                path = normalizePath(body.getPath());
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(sosHibernateSession);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();

            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setPath(path);
            filterJoeObjects.setOrderCriteria("created");
            if (isDirectory) {
                filterJoeObjects.setRecursive();
            }

            List<DBItemJoeObject> listOfJoeObjects = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);
            JOCHotFolder jocHotFolder = new JOCHotFolder(this);

            for (DBItemJoeObject joeObject : listOfJoeObjects) {
                
                DeployJoeAudit deployJoeAudit = new DeployJoeAudit(joeObject, body);
                logAuditMessage(deployJoeAudit);

                if ("FOLDER".equals(joeObject.getObjectType())) {

                    if (!folderPermissions.isPermittedForFolder(joeObject.getPath())) {
                        return accessDeniedResponse();
                        //continue; ... maybe better?
                    }

                    switch (joeObject.getOperation().toLowerCase()) {
                    case "store":
                        jocHotFolder.putFolder(normalizeFolder(joeObject.getPath()));
                        break;
                    case "delete":
                        jocHotFolder.deleteFolder(normalizeFolder(joeObject.getPath()));
                        break;

                    default:
                        break;
                    }
                } else {
                    if (!folderPermissions.isPermittedForFolder(getParent(joeObject.getPath()))) {
                        return accessDeniedResponse();
                        //continue; ... maybe better?
                    }

                    if (!Helper.CLASS_MAPPING.containsKey(joeObject.getObjectType())) {
                        throw new JobSchedulerBadRequestException("unsupported objectType found in database: " + joeObject.getObjectType());
                        //continue; ... maybe better?
                    }

                    String extension = Helper.getFileExtension(JobSchedulerObjectType.fromValue(joeObject.getObjectType()));

                    switch (joeObject.getOperation().toLowerCase()) {
                    case "store":
                        jocHotFolder.putFile(joeObject.getPath() + extension, this.getPojoAsByte(joeObject));
                        break;
                    case "delete":
                        jocHotFolder.deleteFile(joeObject.getPath() + extension);
                        break;
                    default:
                        break;
                    }
                }
                
                storeAuditLogEntry(deployJoeAudit);
                
                //TODO update other cluster members if (!dbItemInventoryInstance.standalone())
                
                dbLayerJoeObjects.delete(joeObject);
            }
            Globals.commit(sosHibernateSession);

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback(sosHibernateSession);
            Globals.disconnect(sosHibernateSession);
        }
    }

    private byte[] getPojoAsByte(DBItemJoeObject joeObject) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n";
        final byte[] bytes = Globals.xmlMapper.writeValueAsBytes(Globals.objectMapper.readValue(joeObject.getConfiguration(),
                Helper.CLASS_MAPPING.get(joeObject.getObjectType())));

        return Helper.concatByteArray(xmlHeader.getBytes(), bytes);
    }
}
