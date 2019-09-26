package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocObjectAlreadyExistException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IRenameResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.JSObjectEdit;

@Path("joe")
public class RenameResourceImpl extends JOCResourceImpl implements IRenameResource {

    private static final String API_CALL = "./joe/delete";

    @Override
    public JOCDefaultResponse rename(final String accessToken, final byte[] responseBody) {
        SOSHibernateSession connection = null;
        try {
            JSObjectEdit body = Globals.objectMapper.readValue(responseBody, JSObjectEdit.class);
            checkRequiredParameter("objectType", body.getObjectType());
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken); 
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
            boolean permission2 = Helper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("path", body.getPath());
            checkRequiredParameter("oldPath", body.getOldPath());
            String path = normalizePath(body.getPath());
            String oldPath = normalizePath(body.getOldPath());
            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;

            if (isDirectory) {
                if (!this.folderPermissions.isPermittedForFolder(path)) {
                    return accessDeniedResponse();
                }

            } else {
                if (!this.folderPermissions.isPermittedForFolder(getParent(path))) {
                    return accessDeniedResponse();
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);
            
            if (item != null) {
                throw new JocObjectAlreadyExistException(path);
            }
            
            filter.setPath(oldPath);
            item = dbLayer.getJoeObject(filter);
            
            if (item != null) {
                item.setOperation("rename");
                item.setAccount(getAccount());
                item.setPath(path);
                if (!isDirectory) {
                    item.setConfiguration(Globals.objectMapper.writeValueAsString(body.getConfiguration()));
                } else {
                    item.setConfiguration(null);
                }
                dbLayer.update(item);
                if (isDirectory) {
                    //TODO recursive rename
                }

            } else {  //if old item doesn't exist then really create an item??
                item = new DBItemJoeObject();
                item.setId(null);
                item.setAccount(getAccount());
                item.setSchedulerId(body.getJobschedulerId());
                item.setAuditLogId(null);
                if (!isDirectory) {
                    item.setConfiguration(Globals.objectMapper.writeValueAsString(body.getConfiguration()));
                } else {
                    item.setConfiguration(null);
                }
                item.setObjectType(body.getObjectType().value());
                item.setOperation("store");
                item.setPath(body.getPath());
                dbLayer.save(item);
            }
            

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
