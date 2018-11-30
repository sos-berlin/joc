package com.sos.joc.classes.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sos.jitl.reporting.db.DBItemInventoryLock;
import com.sos.joc.db.inventory.locks.InventoryLocksDBLayer;
import com.sos.joc.model.lock.LockP;


public class LockPermanent {

    public static List<LockP> getListOfLocksToAdd(InventoryLocksDBLayer dbLayer, Map<String, String> documentations, List<DBItemInventoryLock> locksFromDb, String regex)
            throws Exception {
        List<LockP> locksToAdd = new ArrayList<LockP>();
        if (locksFromDb != null) {
            for (DBItemInventoryLock lockFromDb : locksFromDb) {
                if (regex != null && !regex.isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(lockFromDb.getName());
                    if (regExMatcher.find()) {
                        locksToAdd.add(getLockP(dbLayer, documentations.get(lockFromDb.getName()), lockFromDb));
                    }
                } else {
                    locksToAdd.add(getLockP(dbLayer, documentations.get(lockFromDb.getName()), lockFromDb));
                }
            }
        }
        return locksToAdd;
    }

    public static LockP getLockP(InventoryLocksDBLayer dbLayer, String documentation, DBItemInventoryLock lockFromDb) throws Exception {
        if (lockFromDb != null) {
            LockP lockP = new LockP();
            lockP.setMaxNonExclusive(lockFromDb.getMaxNonExclusive());
            lockP.setName(lockFromDb.getBasename());
            lockP.setPath(lockFromDb.getName());
            lockP.setDocumentation(documentation);
            lockP.setSurveyDate(lockFromDb.getModified());
            lockP.setConfigurationDate(dbLayer.getLockConfigurationDate(lockFromDb.getId()));
            return lockP;
        } else {
            return null;
        }
    }
    
}
