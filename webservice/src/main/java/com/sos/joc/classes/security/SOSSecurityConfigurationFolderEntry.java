package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.model.security.SecurityConfigurationFolder;

public class SOSSecurityConfigurationFolderEntry {

    private String[] listOfFolderEntries;
    private String master;
    private String role;
    private List<String> listOWriteFolders;


    public SOSSecurityConfigurationFolderEntry(String key, String entry) {
        super();
        listOfFolderEntries = entry.split(",");
        listOWriteFolders = new ArrayList<String>();
        String[] keys = key.split("\\|");
        if (keys.length > 1) {
            master = keys[0];
            role = keys[1];
        } else {
            master = "";
            role = keys[0];
        }
    }

    public SOSSecurityConfigurationFolderEntry(String key) {
        super();
        listOWriteFolders = new ArrayList<String>();

        String[] keys = key.split("\\|");
        if (keys.length > 1) {
            master = keys[0];
            role = keys[1];
        } else {
            master = "";
            role = keys[0];
        }
    }

    
    public static String getFolderKey(String master, String role) {
        if (!"".equals(master)){
            return master + "|" + role;
        }else{
            return role;
            
       }
    }

    public void addFolder(String folder) {
        listOWriteFolders.add(folder);
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

    public String getIniWriteString() {
        String folderKey = SOSSecurityConfigurationFolderEntry.getFolderKey(master, role);
        String s = "";
        for (int i=0;i<listOWriteFolders.size();i++){
           s=s +listOWriteFolders.get(i);
           if (i<listOWriteFolders.size()-1){
               s=s + ", \\" + "\n" + "                                                                                           ".substring(0, folderKey.length()+3);          
           }else{
               s = s + "\n";
           }
        }
        return s;
    }

}
