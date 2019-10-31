package com.sos.joc.db.joe;

import com.sos.joc.model.tree.Tree;

public class LocksByFolder extends Tree {
    
    public LocksByFolder(String folder, String account) {
        setPath(folder);
        setLockedBy(account);
    }
}
