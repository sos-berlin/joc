package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeLock;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.resource.ILockResource;
import com.sos.joc.model.joe.lock.LockFilter;
import com.sos.joc.model.joe.lock.LockInfo;

@Path("joe")
public class LockResourceImpl extends JOCResourceImpl implements ILockResource {

    private static final String API_CALL_LOCK = "./joe/lock";
    private static final String API_CALL_RELEASE = "./joe/lock/release";
    private static final String API_CALL_LOCKED = "./joe/lock/info";

    @Override
    public JOCDefaultResponse lock(final String accessToken, final LockFilter body) {
        return lockOrReleaseOrInfo(accessToken, body, true, API_CALL_LOCK);
    }

    @Override
    public JOCDefaultResponse release(String accessToken, LockFilter body) {
        return lockOrReleaseOrInfo(accessToken, body, false, API_CALL_RELEASE);
    }

    @Override
    public JOCDefaultResponse lockedBy(String accessToken, LockFilter body) {
        return lockOrReleaseOrInfo(accessToken, body, null, API_CALL_LOCKED);
    }

    private JOCDefaultResponse lockOrReleaseOrInfo(String accessToken, LockFilter body, Boolean lock, String apiCall) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(apiCall, body, accessToken, body.getJobschedulerId(), getPermissonsJocCockpit(body
                    .getJobschedulerId(), accessToken).getJobschedulerMaster().getAdministration().getConfigurations().isView());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            if (body.getFolder() == null || body.getFolder().isEmpty()) {
               body.setFolder(body.getPath()); //path is alias for folder
            }
            checkRequiredParameter("folder", body.getFolder());
            body.setFolder(normalizeFolder(body.getFolder()));
            if (!folderPermissions.isPermittedForFolder(body.getFolder())) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(apiCall);
            DBItemJoeLock dbItem = lockOrReleaseOrInfo(new DBLayerJoeLocks(sosHibernateSession), body.getJobschedulerId(), body.getFolder(), lock,
                    body.getForceLock(), getAccount());
            if (lock != null) {
                try {
                    CustomEvent evt = JOEHelper.getJoeUpdatedEvent(body.getFolder(), "FOLDER");
                    SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
                } catch (Exception e) {
                }
            }

            LockInfo entity = new LockInfo();
            if (dbItem != null && dbItem.getIsLocked()) {
                entity.setLockedBy(dbItem.getAccount());
                entity.setLockedSince(dbItem.getModified());
                entity.setIsLocked(true);
            }
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JoeFolderAlreadyLockedException e) {
            LockInfo entity = new LockInfo();
            entity.setIsLocked(true);
            entity.setLockedSince(e.getLockedSince());
            entity.setLockedBy(e.getLockedBy());
            entity.setDeliveryDate(Date.from(Instant.now()));
            //e.addErrorMetaInfo(getJocError());
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

    public static DBItemJoeLock release(DBLayerJoeLocks dbLayerJoeLocks, String schedulerId, String path, String account)
            throws DBConnectionRefusedException, DBInvalidDataException, JoeFolderAlreadyLockedException {
        return lockOrReleaseOrInfo(dbLayerJoeLocks, schedulerId, path, false, true, account);
    }
    
    public static DBItemJoeLock unForcelock(DBLayerJoeLocks dbLayerJoeLocks, String schedulerId, String path, String account)
            throws DBConnectionRefusedException, DBInvalidDataException, JoeFolderAlreadyLockedException {
        return lockOrReleaseOrInfo(dbLayerJoeLocks, schedulerId, path, true, false, account);
    }
    
    public static DBItemJoeLock forcelock(DBLayerJoeLocks dbLayerJoeLocks, String schedulerId, String path, String account)
            throws DBConnectionRefusedException, DBInvalidDataException, JoeFolderAlreadyLockedException {
        return lockOrReleaseOrInfo(dbLayerJoeLocks, schedulerId, path, true, true, account);
    }

    public static DBItemJoeLock lockOrReleaseOrInfo(DBLayerJoeLocks dbLayerJoeLocks, String schedulerId, String path, Boolean lock, Boolean forceLock,
            String account) throws DBConnectionRefusedException, DBInvalidDataException, JoeFolderAlreadyLockedException {
        DBItemJoeLock dbItem = dbLayerJoeLocks.getJoeLock(schedulerId, path);
        if (lock != null) {
            if (dbItem == null) {
                if (lock) {
                    dbItem = new DBItemJoeLock();
                    dbItem.setAccount(account);
                    dbItem.setIsLocked(lock);
                    dbItem.setFolder(path);
                    dbItem.setSchedulerId(schedulerId);
                    dbLayerJoeLocks.save(dbItem);
                }
            } else {
                if (dbItem.getIsLocked() && !forceLock && !account.equals(dbItem.getAccount())) {
                    throw new JoeFolderAlreadyLockedException(dbItem.getModified(), dbItem.getAccount());
                }
                if (lock == dbItem.getIsLocked() && account.equals(dbItem.getAccount())) {
                    return dbItem;
                }
                dbItem.setAccount(account);
                dbItem.setIsLocked(lock);
                dbLayerJoeLocks.update(dbItem);
            }
        }
        return dbItem;
    }

}
