package com.sos.joc.joe.impl;

import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.resource.IDeployablesResource;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Deployable;
import com.sos.joc.model.joe.common.Deployables;
import com.sos.schema.JsonValidator;

@Path("joe")
public class DeployablesResourceImpl extends JOCResourceImpl implements IDeployablesResource {

    private static final String API_CALL = "./joe/deployables";

    @Override
    public JOCDefaultResponse deployables(final String accessToken, final byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobSchedulerId.class);
            JobSchedulerId body = Globals.objectMapper.readValue(filterBytes, JobSchedulerId.class);
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJob() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJobChain() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isLock() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isMonitor() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isOrder() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isProcessClass() ||
            sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isSchedule();

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();

            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setOrderCriteria("created");

            List<DBItemJoeObject> listOfJoeObjects = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);

            Deployables deployables = new Deployables();
            if (listOfJoeObjects != null) {
                deployables.setDeployables(listOfJoeObjects.stream().filter(joeObject -> {
                    if (!JOEHelper.CLASS_MAPPING.containsKey(joeObject.getObjectType())) {
                        return false;
                    }
                    if ("FOLDER".equals(joeObject.getObjectType())) {
                        if (!folderPermissions.isPermittedForFolder(joeObject.getPath())) {
                            return false;
                        }
                    } else {
                        if (!folderPermissions.isPermittedForFolder(getParent(joeObject.getPath()))) {
                            return false;
                        }
                        if (!joeObject.operationIsDelete() && joeObject.getConfiguration() == null) {
                            return false;
                        }
                    }
                    return true;
                }).map(joeObject -> {
                    Deployable deployable = new Deployable();
                    deployable.setAccount(joeObject.getAccount());
                    deployable.setFolder(getParent(joeObject.getPath()));
                    deployable.setJobschedulerId(joeObject.getSchedulerId());
                    deployable.setModified(joeObject.getModified());
                    deployable.setObjectName(Paths.get(joeObject.getPath()).getFileName().toString());
                    deployable.setOperation(joeObject.getOperation());
                    JobSchedulerObjectType objType = JobSchedulerObjectType.fromValue(joeObject.getObjectType());
                    if (objType == JobSchedulerObjectType.NODEPARAMS) {
                        if (deployable.getObjectName().contains(",")) {
                            objType = JobSchedulerObjectType.ORDER;
                        } else {
                            objType = JobSchedulerObjectType.JOBCHAIN;
                        }
                    }
                    deployable.setObjectType(objType);
                    return deployable;
                }).collect(Collectors.toSet()));
            } else {
                deployables.setDeployables(new HashSet<Deployable>());
            }
            deployables.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(deployables);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
}
