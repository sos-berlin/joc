package com.sos.joc.db.joe;

import java.nio.file.Paths;

public class FoldersByPath {
    
    private String folder;

    public FoldersByPath(String path, String objectType) {
        if ("FOLDER".equals(objectType)) {
            this.folder = path;
        } else {
            this.folder = Paths.get(path).getParent().toString().replace('\\', '/');
        }
    }

    public String getFolder() {
        return folder;
    }
}
