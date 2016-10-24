package com.sos.joc.locks.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(LocksResourcePImpl.class);
    private String regex;
    private List<Folder> folders;
    private List<LockPath> locks;

    @Override
    public JOCDefaultResponse postLocksP(String accessToken, LocksFilter locksFilter) throws Exception {
        LOGGER.debug("init locks");
        try {
            JOCDefaultResponse jocDefaultResponse = init(locksFilter.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // FILTER
            locks = locksFilter.getLocks();
            folders = locksFilter.getFolders();
            regex = locksFilter.getRegex();
            InventoryInstancesDBLayer instanceDBLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceDBLayer.getInventoryInstanceBySchedulerId(locksFilter.getJobschedulerId());
            InventoryLocksDBLayer dbLayer = new InventoryLocksDBLayer(Globals.sosHibernateConnection);
            LocksP entity = new LocksP();
            entity.setDeliveryDate(Date.from(Instant.now()));
            List<LockP> listOfLocks = new ArrayList<LockP>();
            if (locks != null && !locks.isEmpty()) {
                List<LockP> locksToAdd = new ArrayList<LockP>();
                for(LockPath lockPath :locks) {
                    DBItemInventoryLock lockFromDb = dbLayer.getLock(lockPath.getLock(), instance.getId());
                    LockP lock = getLockP(dbLayer, lockFromDb);
                    if (lock != null) {
                        locksToAdd.add(lock);
                    }
                }
                if (locksToAdd != null && !locksToAdd.isEmpty()) {
                    listOfLocks.addAll(locksToAdd);
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryLock> locksFromDb = dbLayer.getLocksByFolders(folder.getFolder(), instance.getId(), folder.getRecursive().booleanValue());
                    List<LockP> locksToAdd = getListOfLocksToAdd(dbLayer, locksFromDb);
                    if(locksToAdd != null && !locksToAdd.isEmpty()){
                        listOfLocks.addAll(locksToAdd);
                    }
                }
            } else {
                List<DBItemInventoryLock> locksFromDb = dbLayer.getLocks(instance.getId());
                List<LockP> locksToAdd = getListOfLocksToAdd(dbLayer, locksFromDb);
                if(locksToAdd != null && !locksToAdd.isEmpty()){
                    listOfLocks.addAll(locksToAdd);
                }
            }
            entity.setLocks(listOfLocks);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private List<LockP> getListOfLocksToAdd(InventoryLocksDBLayer dbLayer, List<DBItemInventoryLock> locksFromDb) throws Exception {
        List<LockP> locksToAdd = new ArrayList<LockP>();
        if (locksFromDb != null) {
            for (DBItemInventoryLock lockFromDb : locksFromDb) {
                if (regex != null && !regex.isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(lockFromDb.getName());
                    if (regExMatcher.find()) {
                        locksToAdd.add(getLockP(dbLayer, lockFromDb));
                    }
                } else {
                    locksToAdd.add(getLockP(dbLayer, lockFromDb));
                }
            }
        }
        return locksToAdd;
    }

    private LockP getLockP(InventoryLocksDBLayer dbLayer, DBItemInventoryLock lockFromDb) throws Exception {
        if (lockFromDb != null) {
            LockP lockP = new LockP();
            lockP.setMaxNonExclusive(lockFromDb.getMaxNonExclusive());
            lockP.setName(lockFromDb.getBasename());
            lockP.setPath(lockFromDb.getName());
            lockP.setSurveyDate(lockFromDb.getModified());
            lockP.setConfigurationDate(dbLayer.getLockConfigurationDate(lockFromDb.getId()));
            return lockP;
        } else {
            return null;
        }
    }
    
}