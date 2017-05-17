package com.sos.joc.classes.security;

import com.sos.joc.model.security.SecurityConfigurationFolder;

public class SOSSecurityFolderItem {

    private boolean recursive;
    private String folder;
    private String normalizedFolder;

    public SOSSecurityFolderItem(String folder) {
        super();
        this.folder = folder.trim();
        this.normalizedFolder = normalizeFolder();
    }

    public SOSSecurityFolderItem(String master, SecurityConfigurationFolder securityConfigurationFolder) {
        super();
        this.folder = securityConfigurationFolder.getFolder();
        this.normalizedFolder = this.normalizeFolder();
        this.recursive = securityConfigurationFolder.getRecursive();
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

    public String getIniValue() {
        String s = folder;
        if (isRecursive()) {
            s = s + "/*";
        }
        return s;

    }

}
