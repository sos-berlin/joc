package com.sos.joc.classes.security;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import com.sos.joc.Globals;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.model.security.SecurityConfigurationFolder;
import com.sos.joc.model.security.SecurityConfigurationMaster;
import com.sos.joc.model.security.SecurityConfigurationPermission;
import com.sos.joc.model.security.SecurityConfigurationRole;
import com.sos.joc.model.security.SecurityConfigurationUser;

public class SOSSecurityConfiguration {

    private Ini ini;
    private Wini writeIni;
    private SecurityConfiguration securityConfiguration = new SecurityConfiguration();
    private SOSSecurityConfigurationMasters listOfMasters;

    public SOSSecurityConfiguration(){
        super();
        securityConfiguration = new SecurityConfiguration();
        ini = Ini.fromResourcePath(Globals.getShiroIniInClassPath());
        listOfMasters = SOSSecurityConfigurationMasters.getInstance();
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

        Section s = ini.getSection("folders");
        if (s != null) {
            for (String role2Master : s.keySet()) {
                SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = new SOSSecurityConfigurationFolderEntry(role2Master, s.get(
                        role2Master));
                sosSecurityConfigurationFolderEntry.addFolders();
            }
        }
    }

    public SecurityConfiguration readConfiguration() {
        addUsers();
        addRoles();
        addFolders();

        listOfMasters.createConfigurations(securityConfiguration);
        return this.securityConfiguration;
    }

    private void writeUsers() {
        writeIni.get("users").clear();
        for (SecurityConfigurationUser securityConfigurationUser : securityConfiguration.getUsers()) {
            SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry(securityConfigurationUser);
            writeIni.get("users").put(securityConfigurationUser.getUser(),sosSecurityConfigurationUserEntry.getIniWriteString());
        }
    }

    private void writeMasters() {
        writeIni.get("roles").clear();
//        ini.getSection("folders").clear();
        
        
        Map<String, SOSSecurityConfigurationRoleEntry> roles = new HashMap<String,SOSSecurityConfigurationRoleEntry>();
        for (SecurityConfigurationMaster securityConfigurationMaster : securityConfiguration.getMasters()) {
            String master = securityConfigurationMaster.getMaster();
            for (SecurityConfigurationRole securityConfigurationRole : securityConfigurationMaster.getRoles()) {
                String role = securityConfigurationRole.getRole();
                if (roles.get(role) == null){
                    SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = new SOSSecurityConfigurationRoleEntry(role);
                    roles.put(role,sosSecurityConfigurationRoleEntry);
                }

                for (SecurityConfigurationFolder securityConfigurationFolder : securityConfigurationRole.getFolders()) {
              //      ini.get("folders").put(securityConfigurationMaster.getMaster() + "|" + securityConfigurationRole.getRole(),
              //              securityConfigurationFolder.getFolder() + "/*, \\");
                }
                for (SecurityConfigurationPermission securityConfigurationPermission : securityConfigurationRole.getPermissions()) {
                    SOSSecurityPermissionItem sosSecurityPermissionItem = new SOSSecurityPermissionItem(master, securityConfigurationPermission);
                    roles.get(role).addPermission(sosSecurityPermissionItem.getIniValue());
                }
            }
        }
        
        for (String role : roles.keySet()) {
            SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = roles.get(role);
            writeIni.get("roles").put(role, sosSecurityConfigurationRoleEntry.getIniWriteString());
        }
    }

    public SecurityConfiguration writeConfiguration(SecurityConfiguration securityConfiguration) throws IOException {
        writeIni = new Wini(new File(Globals.getShiroIniFileName()));

        this.securityConfiguration = securityConfiguration;
        writeUsers();
        writeMasters();
        writeIni.store();
        
        return this.securityConfiguration;
    }
}
