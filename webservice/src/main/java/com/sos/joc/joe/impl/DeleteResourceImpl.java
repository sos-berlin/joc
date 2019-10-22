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
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IDeleteResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Filter;

@Path("joe")
public class DeleteResourceImpl extends JOCResourceImpl implements IDeleteResource {

    private static final String API_CALL = "./joe/delete";

    @Override
    public JOCDefaultResponse delete(final String accessToken, final Filter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            checkRequiredParameter("objectType", body.getObjectType());

            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isDelete();
            boolean permission2 = Helper.hasPermission(body.getObjectType(), sosPermissionJocCockpit);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1 && permission2);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;

            if (isDirectory) {
                body.setPath(normalizeFolder(body.getPath()));
                if (!folderPermissions.isPermittedForFolder(body.getPath())) {
                    return accessDeniedResponse();
                }
            } else {
                body.setPath(normalizePath(body.getPath()));
                if (!folderPermissions.isPermittedForFolder(getParent(body.getPath()))) {
                    return accessDeniedResponse();
                }
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);

            Globals.beginTransaction(sosHibernateSession);
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
                dbLayer.save(item);
            }
            if (isDirectory) {
                filter = new FilterJoeObjects();
                filter.setSchedulerId(item.getSchedulerId());
                filter.setPath(item.getPath());
                filter.setRecursive();
                dbLayer.updateFolderCommand(filter, "delete");
            }
            Globals.commit(sosHibernateSession);
            
            try {
                CustomEvent evt = Helper.sendEvent(body.getPath(), body.getObjectType().value());
                SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
            } catch (Exception e) {
                //
            }

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
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
