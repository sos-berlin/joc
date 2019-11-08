package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;
import javax.ws.rs.Path;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.resource.IDeleteDraftResource;
import com.sos.joc.model.joe.common.FilterDeploy;
import com.sos.joc.model.joe.lock.LockInfo;

@Path("joe")
public class DeleteDraftResourceImpl extends JOCResourceImpl implements IDeleteDraftResource {

    private static final String API_CALL = "./joe/deletedraft";

    @Override
    public JOCDefaultResponse deleteDraft(final String accessToken, final FilterDeploy body) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission1 = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isDelete();
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission1);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("folder", body.getFolder());

            body.setFolder(normalizeFolder(body.getFolder()));
            if (!folderPermissions.isPermittedForFolder(getParent(body.getFolder()))) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            LockResourceImpl.unForcelock(new DBLayerJoeLocks(sosHibernateSession), body.getJobschedulerId(), body.getFolder(), getAccount());

            sosHibernateSession.setAutoCommit(false);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filter = new FilterJoeObjects();
            if (body.getAccount() == null || body.getAccount().isEmpty()) {
                filter.setAccount(getAccount());
            } else {
                filter.setAccount(body.getAccount());
            }

            if (body.getObjectName() != null) {
                filter.setPath((body.getFolder() + "/").replaceAll("//+", "/") + body.getObjectName());
            } else {
                filter.setPath(body.getFolder());
            }

            filter.setSchedulerId(body.getJobschedulerId());
            if ((body.getObjectName() == null || body.getObjectName().isEmpty()) && (body.getRecursive() != null && body.getRecursive())) {
                filter.setRecursive();
            }

            filter.setObjectType(body.getObjectType());

            Globals.beginTransaction(sosHibernateSession);
            dbLayer.delete(filter);
            Globals.commit(sosHibernateSession);

            try {
                CustomEvent evt = JOEHelper.getJoeUpdatedEvent(body.getFolder(), body.getObjectType().value());
                SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
            } catch (Exception e) {
                //
            }

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JoeFolderAlreadyLockedException e) {
            // e.addErrorMetaInfo(getJocError());
            LockInfo entity = new LockInfo();
            entity.setIsLocked(true);
            entity.setLockedSince(e.getLockedSince());
            entity.setLockedBy(e.getLockedBy());
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus434(entity);
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
