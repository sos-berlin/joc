package com.sos.joc.classes.security;

public class SOSSecurityFolderItem {

    private boolean recursive;
    private String folder;
    private String normalizedFolder;

    public SOSSecurityFolderItem(String folder) {
        super();
        this.folder = folder.trim();
        this.normalizedFolder = normalizeFolder();
    }

    
    public boolean isRecursive() {
        return recursive;
    }

    public String getFolder() {
        return folder;
    }

    public String getNormalizedFolder() {
        return normalizedFolder;
    }

    private String normalizeFolder() {
        if (folder.endsWith("/*")) {
            String f = folder;
            folder = ("/" + f.trim()).replaceAll("//+", "/").replaceFirst("/\\*$", "");
            recursive = true;
        }
        return folder;
    }


}
