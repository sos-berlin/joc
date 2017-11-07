package com.sos.joc.classes.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile;
import org.ini4j.Wini;

import com.sos.joc.Globals;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.model.security.SecurityConfigurationFolder;
import com.sos.joc.model.security.SecurityConfigurationMaster;
import com.sos.joc.model.security.SecurityConfigurationPermission;
import com.sos.joc.model.security.SecurityConfigurationRole;
import com.sos.joc.model.security.SecurityConfigurationUser;

public class SOSSecurityConfiguration {

    private static final String SECTION_USERS = "users";
    private static final String SECTION_ROLES = "roles";
    private static final String SECTION_FOLDERS = "folders";
    private static final String SECTION_MAIN = "main";

    private Ini ini;
    private Wini writeIni;
    private SecurityConfiguration securityConfiguration = new SecurityConfiguration();
    private SOSSecurityConfigurationMasters listOfMasters;

    public SOSSecurityConfiguration() {
        super();
        securityConfiguration = new SecurityConfiguration();
        ini = Ini.fromResourcePath(Globals.getShiroIniInClassPath());
        listOfMasters = SOSSecurityConfigurationMasters.getInstance();
    }

    private void addUsers() {

        Section s = getSection(SECTION_USERS);

        for (String user : s.keySet()) {
            SecurityConfigurationUser securityConfigurationUser = new SecurityConfigurationUser();
            SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry(s.get(user), null, null);
            securityConfigurationUser.setUser(user);
            securityConfigurationUser.setPassword(sosSecurityConfigurationUserEntry.getPassword());
            securityConfigurationUser.setRoles(sosSecurityConfigurationUserEntry.getRoles());
            securityConfiguration.getUsers().add(securityConfigurationUser);
        }
    }

    private void addRoles() {

        Section s = getSection(SECTION_ROLES);
        for (String role : s.keySet()) {
            SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = new SOSSecurityConfigurationRoleEntry(role, s.get(role));
            sosSecurityConfigurationRoleEntry.addPermissions();
        }
    }

    private void addFolders() {

        Section s = getSection(SECTION_FOLDERS);
        if (s != null) {
            for (String role2Master : s.keySet()) {
                SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = new SOSSecurityConfigurationFolderEntry(role2Master, s.get(
                        role2Master));
                sosSecurityConfigurationFolderEntry.addFolders();
            }
        }
    }

    private void writeUsers() throws InvalidFileFormatException, IOException {
        Wini oldWriteIni;
        oldWriteIni = new Wini(Globals.getShiroIniFile().toFile());
        Profile.Section oldSection = oldWriteIni.get(SECTION_USERS);
        clearSection(SECTION_USERS);
        Profile.Section s = writeIni.get(SECTION_USERS);
        SOSSecurityHashSettings sosSecurityHashSettings = new SOSSecurityHashSettings();
        sosSecurityHashSettings.setMain(getSection(SECTION_MAIN));

        for (SecurityConfigurationUser securityConfigurationUser : securityConfiguration.getUsers()) {
            SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry(securityConfigurationUser,
                    oldSection, sosSecurityHashSettings);
            if ((securityConfigurationUser.getPassword() != null && !securityConfigurationUser.getPassword().isEmpty()) || securityConfigurationUser
                    .getRoles().size() > 0) {
                s.put(securityConfigurationUser.getUser(), sosSecurityConfigurationUserEntry.getIniWriteString());
            }
        }
    }

    private void clearSection(String section) {
        if (writeIni.get(section) != null) {
            writeIni.get(section).clear();
        } else {
            writeIni.add(section);
        }
    }

    private Section getSection(String section) {
        Section s = ini.addSection(section);
        return s;
    }

    private void writeMasters() {
        clearSection(SECTION_ROLES);
        clearSection(SECTION_FOLDERS);

        Map<String, SOSSecurityConfigurationRoleEntry> roles = new HashMap<String, SOSSecurityConfigurationRoleEntry>();
        Map<String, SOSSecurityConfigurationFolderEntry> folders = new HashMap<String, SOSSecurityConfigurationFolderEntry>();

        for (SecurityConfigurationMaster securityConfigurationMaster : securityConfiguration.getMasters()) {
            String master = securityConfigurationMaster.getMaster();
            for (SecurityConfigurationRole securityConfigurationRole : securityConfigurationMaster.getRoles()) {
                String role = securityConfigurationRole.getRole();
                String folderKey = SOSSecurityConfigurationFolderEntry.getFolderKey(master, role);
                if (roles.get(role) == null) {
                    SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = new SOSSecurityConfigurationRoleEntry(role);
                    roles.put(role, sosSecurityConfigurationRoleEntry);
                }

                if (folders.get(folderKey) == null) {
                    SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = new SOSSecurityConfigurationFolderEntry(folderKey);
                    folders.put(folderKey, sosSecurityConfigurationFolderEntry);
                }

                for (SecurityConfigurationFolder securityConfigurationFolder : securityConfigurationRole.getFolders()) {
                    SOSSecurityFolderItem sosSecurityFolderItem = new SOSSecurityFolderItem(master, securityConfigurationFolder);
                    folders.get(folderKey).addFolder(sosSecurityFolderItem.getIniValue());
                }
                for (SecurityConfigurationPermission securityConfigurationPermission : securityConfigurationRole.getPermissions()) {
                    SOSSecurityPermissionItem sosSecurityPermissionItem = new SOSSecurityPermissionItem(master, securityConfigurationPermission);
                    roles.get(role).addPermission(sosSecurityPermissionItem.getIniValue());
                }
            }
        }

        for (String role : roles.keySet()) {
            SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = roles.get(role);
            if (!"".equals(sosSecurityConfigurationRoleEntry.getIniWriteString())) {
                writeIni.get(SECTION_ROLES).put(role, sosSecurityConfigurationRoleEntry.getIniWriteString());
            }
        }
        for (String masterAndRole : folders.keySet()) {
            SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = folders.get(masterAndRole);
            if (!"".equals(sosSecurityConfigurationFolderEntry.getIniWriteString())) {
                writeIni.get(SECTION_FOLDERS).put(masterAndRole, sosSecurityConfigurationFolderEntry.getIniWriteString());
            }
        }
    }

    private boolean roleIsEmpty(String role) {
        for (Map.Entry<String, SecurityConfigurationMaster> entry : listOfMasters.getListOfMasters().entrySet()) {
            SecurityConfigurationMaster securityConfigurationMaster = listOfMasters.getListOfMasters().get(entry.getKey());

            for (SecurityConfigurationRole securityConfigurationRole : securityConfigurationMaster.getRoles()) {
                if (role.equals(securityConfigurationRole.getRole())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addRole(SecurityConfigurationMaster securityConfigurationMaster, String role) {
        SecurityConfigurationRole securityConfigurationRole = new SecurityConfigurationRole();
        securityConfigurationRole.setRole(role);
        securityConfigurationMaster.getRoles().add(securityConfigurationRole);
    }

    private void addEmptyRoles() {

        for (SecurityConfigurationUser securityConfigurationUser : securityConfiguration.getUsers()) {
            for (String role : securityConfigurationUser.getRoles()) {
                if (roleIsEmpty(role)) {
                    SecurityConfigurationMaster defaultMaster = listOfMasters.getDefaultMaster(securityConfiguration);
                    addRole(defaultMaster, role);
                }
            }
        }

    }

    private void writeMain() {
        SOSSecurityConfigurationMainEntry sosSecurityConfigurationMainEntry = new SOSSecurityConfigurationMainEntry();

        Section mainSection = ini.getSection(SECTION_MAIN);
        clearSection(SECTION_MAIN);
        
        for (Map.Entry<String, String> entry : mainSection.entrySet()) {
            writeIni.get(SECTION_MAIN).put(entry.getKey(),sosSecurityConfigurationMainEntry.getIniWriteString(entry));
        }
    }

    public SecurityConfiguration readConfiguration() {
        addUsers();
        addRoles();
        addFolders();

        addEmptyRoles();

        listOfMasters.createConfigurations(securityConfiguration);
        return this.securityConfiguration;
    }

    public SecurityConfiguration writeConfiguration(SecurityConfiguration securityConfiguration) throws IOException {
        writeIni = new Wini(Globals.getShiroIniFile().toFile());

        this.securityConfiguration = securityConfiguration;
        writeUsers();
        writeMasters();
        writeMain();
        writeIni.store();

        return this.securityConfiguration;
    }
}
