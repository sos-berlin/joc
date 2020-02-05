package com.sos.joc.db.joe;

import java.util.Date;

import com.sos.joc.model.tree.JoeTree;

public class LocksByFolder extends JoeTree {
    
    public LocksByFolder(String folder, String account, Date lockedSince) {
        setPath(folder);
        setLockedBy(account);
        setLockedSince(lockedSince);
    }
}
