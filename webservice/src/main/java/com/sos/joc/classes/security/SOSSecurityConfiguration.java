package com.sos.joc.classes.security;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import com.sos.joc.Globals;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.model.security.SecurityConfigurationUser;

public class SOSSecurityConfiguration {

    private Ini ini;
    private SecurityConfiguration securityConfiguration = new SecurityConfiguration();
    private SOSSecurityConfigurationMasters listOfMasters;

    public SOSSecurityConfiguration() {
        super();
        securityConfiguration = new SecurityConfiguration();
        ini = Ini.fromResourcePath(Globals.getShiroIniInClassPath());
        listOfMasters =  SOSSecurityConfigurationMasters.getInstance();
    }

    private void addUsers() {

        Section s = ini.getSection("users");
        if (s != null) {
            for (String user : s.keySet()) {
                SecurityConfigurationUser securityConfigurationUser = new SecurityConfigurationUser();
                SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry(s.get(user));
                securityConfigurationUser.setUser(user);
                securityConfigurationUser.setPassword(sosSecurityConfigurationUserEntry.getPassword());
                securityConfigurationUser.setRoles(sosSecurityConfigurationUserEntry.getRoles());
                securityConfiguration.getUsers().add(securityConfigurationUser);
            }
        }
    }

    private void addRoles() {

        Section s = ini.getSection("roles");
        if (s != null) {
            for (String role : s.keySet()) {
                SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = new SOSSecurityConfigurationRoleEntry(role, s.get(role));
                sosSecurityConfigurationRoleEntry.addPermissions();
            }
        }
    }

    private void addFolders() {

    /*    Section s = ini.getSection("folders");
        if (s != null) {
            for (String role2Master : s.keySet()) {
                SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = new SOSSecurityConfigurationFolderEntry(role2Master, s.get(role2Master));
                SecurityConfigurationMasters securityConfigurationMasters = listOfMasters.get(sosSecurityConfigurationFolderEntry.getMaster());
                if (securityConfigurationMasters == null) {
                    securityConfigurationMasters = new SecurityConfigurationMasters();
                    listOfMasters.put(sosSecurityConfigurationFolderEntry.getMaster(), securityConfigurationMasters);
                }
                securityConfigurationMasters.setMaster(sosSecurityConfigurationFolderEntry.getMaster());
                securityConfigurationMasters.setRoles(sosSecurityConfigurationFolderEntry.getRoles());
                listOfMasters.put(sosSecurityConfigurationFolderEntry.getMaster(), securityConfigurationMasters);
            }
        }
        */
    }

    public SecurityConfiguration readConfiguration() {
        addUsers();
        addRoles();
        addFolders();
        
        listOfMasters.createConfigurations(securityConfiguration);
        return this.securityConfiguration;
    }
}
