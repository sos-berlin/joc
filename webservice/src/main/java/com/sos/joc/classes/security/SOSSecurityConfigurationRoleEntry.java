package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.model.security.SecurityConfigurationPermission;

public class SOSSecurityConfigurationRoleEntry {

    private String[] listOfPermissions;
    private String role;

    public SOSSecurityConfigurationRoleEntry(String role, String entry) {
        super();
        listOfPermissions = entry.split(",");
        this.role = role;
    }


    public void addPermissions() {
        SOSSecurityConfigurationMasters listOfMasters = SOSSecurityConfigurationMasters.getInstance();

        for (int i = 0; i < listOfPermissions.length; i++) {
            SecurityConfigurationPermission securityConfigurationPermission = new SecurityConfigurationPermission();
            String permission = listOfPermissions[i];
            SOSSecurityPermissionEntry sosSecurityPermissionEntry = new SOSSecurityPermissionEntry(permission);

            String master = sosSecurityPermissionEntry.getMaster();
            listOfMasters.addMaster(master);

            List<SecurityConfigurationPermission> listOfPermissions = listOfMasters.getPermissions(master, role);

            permission = sosSecurityPermissionEntry.getNormalizedPermission();

            securityConfigurationPermission.setExcluded(sosSecurityPermissionEntry.isExcluded());
            securityConfigurationPermission.setPath(permission);
            listOfPermissions.add(securityConfigurationPermission);
        }
    }

  /*  public List<SecurityConfigurationPermission> getPermissions() {
        List<SecurityConfigurationPermission> listOfPermissions = new ArrayList<SecurityConfigurationPermission>();
        for (int i = 0; i < listOfPermissions.length; i++) {
            SecurityConfigurationPermission securityConfigurationPermission = new SecurityConfigurationPermission();
            String path = listOfPermissions[i];
            boolean excluded = false;
            if (path.startsWith("-")) {
                path = listOfPermissions[i].substring(1);
                excluded = true;
            }
            securityConfigurationPermission.setPath(path);
            securityConfigurationPermission.setExcluded(excluded);
            listOfPermissions.add(securityConfigurationPermission);
        }
        return listOfPermissions;
    }
    */
}
