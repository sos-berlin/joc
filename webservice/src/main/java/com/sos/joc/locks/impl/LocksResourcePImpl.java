package com.sos.joc.locks.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.locks.LockPermanent;
import com.sos.joc.db.inventory.locks.InventoryLocksDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.locks.resource.ILocksResourceP;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.lock.LockP;
import com.sos.joc.model.lock.LockPath;
import com.sos.joc.model.lock.LocksFilter;
import com.sos.joc.model.lock.LocksP;

@Path("locks")
public class LocksResourcePImpl extends JOCResourceImpl implements ILocksResourceP {

    private static final String API_CALL = "./locks/p";
    private String regex;
    private List<Folder> folders;
    private List<LockPath> locks;

    @Override
    public JOCDefaultResponse postLocksP(String accessToken, LocksFilter locksFilter) throws Exception {
        SOSHibernateConnection connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, locksFilter, accessToken, locksFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            // FILTER
            locks = locksFilter.getLocks();
            folders = locksFilter.getFolders();
            regex = locksFilter.getRegex();
            InventoryLocksDBLayer dbLayer = new InventoryLocksDBLayer(connection);
            LocksP entity = new LocksP();
            List<LockP> listOfLocks = new ArrayList<LockP>();
            if (locks != null && !locks.isEmpty()) {
                List<LockP> locksToAdd = new ArrayList<LockP>();
                for (LockPath lockPath : locks) {
                    DBItemInventoryLock lockFromDb = dbLayer.getLock(normalizePath(lockPath.getLock()), dbItemInventoryInstance.getId());
                    LockP lock = LockPermanent.getLockP(dbLayer, lockFromDb);
                    if (lock != null) {
                        locksToAdd.add(lock);
                    }
                }
                if (locksToAdd != null && !locksToAdd.isEmpty()) {
                    listOfLocks.addAll(locksToAdd);
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryLock> locksFromDb = null;
                    locksFromDb = dbLayer.getLocksByFolders(normalizeFolder(folder.getFolder()), dbItemInventoryInstance.getId(), folder
                            .getRecursive().booleanValue());
                    List<LockP> locksToAdd = LockPermanent.getListOfLocksToAdd(dbLayer, locksFromDb, regex);
                    if (locksToAdd != null && !locksToAdd.isEmpty()) {
                        listOfLocks.addAll(locksToAdd);
                    }
                }
            } else {
                List<DBItemInventoryLock> locksFromDb = dbLayer.getLocks(dbItemInventoryInstance.getId());
                List<LockP> locksToAdd = LockPermanent.getListOfLocksToAdd(dbLayer, locksFromDb, regex);
                if (locksToAdd != null && !locksToAdd.isEmpty()) {
                    listOfLocks.addAll(locksToAdd);
                }
            }
            entity.setLocks(listOfLocks);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
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