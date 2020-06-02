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
import com.sos.joc.classes.JOCHotFolder;
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
            // It's not allowed to delete the root folder
            if ("/".equals(body.getPath())) {
                throw new JobSchedulerModifyObjectException("Deleting the root directory is not allowed.");
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            LockResourceImpl.unForcelock(new DBLayerJoeLocks(sosHibernateSession), body.getJobschedulerId(), folder, getAccount());

            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
            DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(sosHibernateSession);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject dbItem = dbLayer.getJoeObject(filter);

            if (dbItem != null) {
                dbItem.setOperation("delete");
                dbItem.setAccount(getAccount());
                dbItem.setModified(Date.from(Instant.now()));
                dbLayer.update(dbItem);
            } else {
                dbItem = new DBItemJoeObject();
                dbItem.setCreated(Date.from(Instant.now()));
                dbItem.setId(null);
                dbItem.setAccount(getAccount());
                dbItem.setSchedulerId(body.getJobschedulerId());
                dbItem.setAuditLogId(null);
                dbItem.setConfiguration(null);
                dbItem.setObjectType(body.getObjectType().value());
                dbItem.setOperation("delete");
                dbItem.setPath(body.getPath());
                dbItem.setDocPath(JOEHelper.getDocPath(dbDocLayer, dbItem));
                if ("/".equals(body.getPath())) {
                    dbItem.setFolder(".");
                } else {
                    dbItem.setFolder(folder);
                }
                dbLayer.save(dbItem);
            }

            if (body.getObjectType() == JobSchedulerObjectType.JOBCHAIN) {
                // delete orders, nodeparams
                JOEHelper.deleteOrdersAndNodeParams(dbLayer, dbDocLayer, dbItem, getAccount(), new JOCHotFolder(this));
            } else if (body.getObjectType() == JobSchedulerObjectType.ORDER) {
                // delete nodeparams
                JOEHelper.deleteNodeParams(dbLayer, dbDocLayer, dbItem, getAccount(), new JOCHotFolder(this));
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
}
