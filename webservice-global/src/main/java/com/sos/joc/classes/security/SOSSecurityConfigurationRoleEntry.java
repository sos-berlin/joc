package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.model.security.SecurityConfigurationPermission;

public class SOSSecurityConfigurationRoleEntry {

    private String[] listOfPermissions;
    private String role;
    private List<String> listOWritePermissions;

    public SOSSecurityConfigurationRoleEntry(String role, String entry) {
        super();
        listOfPermissions = entry.split(",");
        listOWritePermissions = new ArrayList<String>();
        this.role = role;
    }

    public SOSSecurityConfigurationRoleEntry(String role) {
        super();
        listOWritePermissions = new ArrayList<String>();
        this.role = role;
    }

    public void addPermission(String permission) {
        listOWritePermissions.add(permission);
    }

    public String getIniWriteString(){
        String s = "";
        for (int i=0;i<listOWritePermissions.size();i++){
           s=s +listOWritePermissions.get(i);
           if (i<listOWritePermissions.size()-1){
               s=s + ", \\" + "\n" + "                                                                    ".substring(0, role.length()+3);          
           }else{
               s = s + "\n";
           }
        }
        return s;
    }

    public void addPermissions() {
        SOSSecurityConfigurationMasters listOfMasters = SOSSecurityConfigurationMasters.getInstance();

        for (int i = 0; i < listOfPermissions.length; i++) {
            SecurityConfigurationPermission securityConfigurationPermission = new SecurityConfigurationPermission();
            String permission = listOfPermissions[i];
            SOSSecurityPermissionItem sosSecurityPermissionItem = new SOSSecurityPermissionItem(permission);

            String master = sosSecurityPermissionItem.getMaster();
            listOfMasters.addMaster(master);

            List<SecurityConfigurationPermission> listOfPermissions = listOfMasters.getPermissions(master, role);

            permission = sosSecurityPermissionItem.getNormalizedPermission();

            securityConfigurationPermission.setExcluded(sosSecurityPermissionItem.isExcluded());
            securityConfigurationPermission.setPath(permission);
            listOfPermissions.add(securityConfigurationPermission);
        }
    }
}
