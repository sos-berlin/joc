package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.model.security.SecurityConfigurationFolder;

public class SOSSecurityConfigurationFolderEntry {

    private String[] values;
    private String master;
    private String role;

    public SOSSecurityConfigurationFolderEntry(String key, String entry) {
        super();
        values = entry.split(",");
        String[] keys = key.split("\\|");
        if (keys.length > 1) {
            master = keys[0];
            role = keys[1];
        } else {
            master = "";
            role = keys[0];
        }

    }

 /*   public List<SecurityConfigurationFolders2Role> getRoles() {
        List<SecurityConfigurationFolders2Role> listOfFolders2Role = new ArrayList<SecurityConfigurationFolders2Role>();
        SecurityConfigurationFolders2Role securityConfigurationFolders2Role = new SecurityConfigurationFolders2Role();
        securityConfigurationFolders2Role.setRole(role);
        for (int i = 0; i < values.length; i++) {
            SecurityConfigurationFolder securityConfigurationFolder = new SecurityConfigurationFolder();
            String folder = values[i];
            boolean recursive = false;
            if (folder.endsWith("*")) {
                String f = values[i];
                folder = ("/" + f.trim()).replaceAll("//+", "/").replaceFirst("/\\*$", "");
                recursive = true;
            }
            securityConfigurationFolder.setFolder(folder);
            securityConfigurationFolder.setRecursive(recursive);
            securityConfigurationFolders2Role.getFolders().add(securityConfigurationFolder);
        }
        listOfFolders2Role.add(securityConfigurationFolders2Role);
        return listOfFolders2Role;
    }
*/
    public String getMaster() {
        return master;
    }
}
