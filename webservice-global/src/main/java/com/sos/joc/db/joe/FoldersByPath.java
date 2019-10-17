package com.sos.joc.db.joe;

import java.nio.file.Paths;

import com.sos.joc.model.tree.Tree;

public class FoldersByPath extends Tree {
    
    public FoldersByPath(String path, String objectType, String operation) {
        if ("FOLDER".equals(objectType)) {
            setPath(path);
        } else {
            setPath(Paths.get(path).getParent().toString().replace('\\', '/'));
        }
        if ("delete".equals(operation)) {
            setDeleted(true);
        }
        setFolders(null);
    }
}
