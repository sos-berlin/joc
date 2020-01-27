package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerModifyObjectException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.resource.IDeleteResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;
import com.sos.schema.JsonValidator;

@Path("joe")
public class DeleteResourceImpl extends JOCResourceImpl implements IDeleteResource {

    private static final String API_CALL = "./joe/delete";

    @Override
    public JOCDefaultResponse delete(final String accessToken, final byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, Filter.class);
            Filter body = Globals.objectMapper.readValue(filterBytes, Filter.class);
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isDelete();
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("objectType", body.getObjectType());
            checkRequiredParameter("path", body.getPath());
            
            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;
            String folder = null;

            if (isDirectory) {
                body.setPath(normalizeFolder(body.getPath()));
                folder = body.getPath();
            } else {
                body.setPath(normalizePath(body.getPath()));
                folder = getParent(body.getPath());
            }
            if (!folderPermissions.isPermittedForFolder(folder)) {
                return accessDeniedResponse();
            }
            //It's not allowed to delete the root folder
            if ("/".equals(body.getPath())) {
                throw new JobSchedulerModifyObjectException("Deleting the root directory is not allowed.");
            }
            
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            LockResourceImpl.unForcelock(new DBLayerJoeLocks(sosHibernateSession), body.getJobschedulerId(), folder, getAccount());

            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);

            if (item != null) {
                item.setOperation("delete");
                item.setAccount(getAccount());
                item.setModified(Date.from(Instant.now()));
                dbLayer.update(item);
            } else {
                item = new DBItemJoeObject();
                item.setCreated(Date.from(Instant.now()));
                item.setId(null);
                item.setAccount(getAccount());
                item.setSchedulerId(body.getJobschedulerId());
                item.setAuditLogId(null);
                item.setConfiguration(null);
                item.setObjectType(body.getObjectType().value());
                item.setOperation("delete");
                item.setPath(body.getPath());
                item.setDocPath(getDocPath(sosHibernateSession, body.getJobschedulerId(), body.getObjectType(), body.getPath()));
                if ("/".equals(body.getPath())) {
                    item.setFolder(".");
                } else {
                    item.setFolder(getParent(body.getPath()));
                }
                dbLayer.save(item);
            }
            
            try {
                CustomEvent evt = JOEHelper.getJoeUpdatedEvent(folder);
                SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
            } catch (Exception e) {
                //
            }

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JoeFolderAlreadyLockedException e) {
            return JOEHelper.get434Response(e);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private String getDocPath(SOSHibernateSession sosHibernateSession, String jobschedulerId, JobSchedulerObjectType type, String path) {
        try {
            if(type == JobSchedulerObjectType.FOLDER) {
                return null;
            }
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(sosHibernateSession);
            return dbLayer.getDocumentationPath(jobschedulerId, type, path);
        } catch (Exception e) {
            return null;
        }
    }

}
