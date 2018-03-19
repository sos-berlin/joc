package com.sos.joc.locks.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
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

    @Override
    public JOCDefaultResponse postLocksP(String xAccessToken, String accessToken, LocksFilter locksFilter) throws Exception {
        return postLocksP(getAccessToken(xAccessToken, accessToken), locksFilter);
    }

    public JOCDefaultResponse postLocksP(String accessToken, LocksFilter locksFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, locksFilter, accessToken, locksFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            // FILTER
            List<LockPath> locks = locksFilter.getLocks();
            String regex = locksFilter.getRegex();
            boolean withFolderFilter = locksFilter.getFolders() != null && !locksFilter.getFolders().isEmpty();
            List<Folder> folders = addPermittedFolder(locksFilter.getFolders());

            InventoryLocksDBLayer dbLayer = new InventoryLocksDBLayer(connection);
            LocksP entity = new LocksP();
            List<LockP> listOfLocks = new ArrayList<LockP>();
            if (locks != null && !locks.isEmpty()) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                for (LockPath lockPath : locks) {
                    if (lockPath != null && canAdd(lockPath.getLock(), permittedFolders)) {
                        DBItemInventoryLock lockFromDb = dbLayer.getLock(normalizePath(lockPath.getLock()), dbItemInventoryInstance.getId());
                        if (lockFromDb == null) {
                            continue;
                        }
                        LockP lock = LockPermanent.getLockP(dbLayer, lockFromDb);
                        listOfLocks.add(lock);
                    }
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                // no permission
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryLock> locksFromDb = dbLayer.getLocksByFolders(normalizeFolder(folder.getFolder()), dbItemInventoryInstance
                            .getId(), folder.getRecursive().booleanValue());
                    listOfLocks.addAll(LockPermanent.getListOfLocksToAdd(dbLayer, locksFromDb, regex));
                }
            } else {
                List<DBItemInventoryLock> locksFromDb = dbLayer.getLocks(dbItemInventoryInstance.getId());
                listOfLocks = LockPermanent.getListOfLocksToAdd(dbLayer, locksFromDb, regex);
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