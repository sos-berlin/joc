package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.model.security.SecurityConfigurationFolder;
import com.sos.joc.model.security.SecurityConfigurationMaster;
import com.sos.joc.model.security.SecurityConfigurationPermission;
import com.sos.joc.model.security.SecurityConfigurationRole;

public class SOSSecurityConfigurationMasters {

    private Map<String, SecurityConfigurationMaster> listOfMasters;

    private static SOSSecurityConfigurationMasters instance;

    protected SOSSecurityConfigurationMasters() {
    }

    public static SOSSecurityConfigurationMasters getInstance() {
        if (instance == null) {
            instance = new SOSSecurityConfigurationMasters();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void createConfigurations(SecurityConfiguration securityConfiguration) {
        for (Map.Entry<String, SecurityConfigurationMaster> entry : listOfMasters.entrySet()) {
            securityConfiguration.getMasters().add(listOfMasters.get(entry.getKey()));
        }

    }

    public SecurityConfigurationMaster getDefaultMaster(SecurityConfiguration securityConfiguration) {
        for (Map.Entry<String, SecurityConfigurationMaster> entry : listOfMasters.entrySet()) {
            SecurityConfigurationMaster securityConfigurationMaster = listOfMasters.get(entry.getKey());
            if ("".equals(securityConfigurationMaster.getMaster())) {
                return securityConfigurationMaster;
            }
        }

        SecurityConfigurationMaster defaultMaster = new SecurityConfigurationMaster();
        listOfMasters.put("", defaultMaster);

        return defaultMaster;
    }

    public SecurityConfigurationMaster get(String master) {
        return listOfMasters.get(master);
    }

    public void addMaster(String master) {
        if (listOfMasters == null) {
            listOfMasters = new HashMap<String, SecurityConfigurationMaster>();
        }

        if (listOfMasters.get(master) == null) {
            SecurityConfigurationMaster securityConfigurationMaster = new SecurityConfigurationMaster();
            securityConfigurationMaster.setMaster(master);
            listOfMasters.put(master, securityConfigurationMaster);
        }
    }

    public List<SecurityConfigurationPermission> getPermissions(String master, String role) {
        SecurityConfigurationMaster securityConfigurationMaster = get(master);
        for (int i = 0; i < securityConfigurationMaster.getRoles().size(); i++) {
            SecurityConfigurationRole securityConfigurationRole = securityConfigurationMaster.getRoles().get(i);
            if (role.equals(securityConfigurationRole.getRole())) {
                return securityConfigurationRole.getPermissions();
            }
        }

        List<SecurityConfigurationPermission> listOfPermissions = new ArrayList<SecurityConfigurationPermission>();
        SecurityConfigurationRole securityConfigurationRole = new SecurityConfigurationRole();
        securityConfigurationRole.setRole(role);
        securityConfigurationRole.setPermissions(listOfPermissions);
        securityConfigurationMaster.getRoles().add(securityConfigurationRole);

        return listOfPermissions;
    }

    public List<SecurityConfigurationFolder> getFolders(String master, String role) {
        SecurityConfigurationMaster securityConfigurationMaster = get(master);
        for (int i = 0; i < securityConfigurationMaster.getRoles().size(); i++) {
            SecurityConfigurationRole securityConfigurationRole = securityConfigurationMaster.getRoles().get(i);
            if (role.equals(securityConfigurationRole.getRole())) {
                return securityConfigurationRole.getFolders();
            }
        }
        List<SecurityConfigurationFolder> listOfFolders = new ArrayList<SecurityConfigurationFolder>();
        SecurityConfigurationRole securityConfigurationRole = new SecurityConfigurationRole();
        securityConfigurationRole.setRole(role);
        securityConfigurationRole.setFolders(listOfFolders);
        securityConfigurationMaster.getRoles().add(securityConfigurationRole);

        return listOfFolders;
    }

    public Map<String, SecurityConfigurationMaster> getListOfMasters() {
        return listOfMasters;
    }
}
