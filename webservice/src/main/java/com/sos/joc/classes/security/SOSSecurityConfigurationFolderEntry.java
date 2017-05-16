package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.model.security.SecurityConfigurationFolder;
import com.sos.joc.model.security.SecurityConfigurationPermission;

public class SOSSecurityConfigurationFolderEntry {

    private String[] listOfFolderEntries;
    private String master;
    private String role;

    public SOSSecurityConfigurationFolderEntry(String key, String entry) {
        super();
        listOfFolderEntries = entry.split(",");
        String[] keys = key.split("\\|");
        if (keys.length > 1) {
            master = keys[0];
            role = keys[1];
        } else {
            master = "";
            role = keys[0];
        }
    }

    public void addFolders() {
        SOSSecurityConfigurationMasters listOfMasters = SOSSecurityConfigurationMasters.getInstance();

        for (int i = 0; i < listOfFolderEntries.length; i++) {
            SecurityConfigurationFolder securityConfigurationFolder = new SecurityConfigurationFolder();
            String folder = listOfFolderEntries[i];
            SOSSecurityFolderItem sosSecurityFolderItem = new SOSSecurityFolderItem(folder);

            listOfMasters.addMaster(master);

            List<SecurityConfigurationFolder> listOfFolders = listOfMasters.getFolders(master, role);

            folder = sosSecurityFolderItem.getNormalizedFolder();

            securityConfigurationFolder.setRecursive(sosSecurityFolderItem.isRecursive());
            securityConfigurationFolder.setFolder(folder);
            listOfFolders.add(securityConfigurationFolder);
        }
    }

    public String getMaster() {
        return master;
    }
}
