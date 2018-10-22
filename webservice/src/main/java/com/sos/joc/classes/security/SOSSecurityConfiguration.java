package com.sos.joc.classes.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile;
import org.ini4j.Wini;

import com.sos.auth.rest.SOSShiroIniShare;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.security.SecurityConfiguration;
import com.sos.joc.model.security.SecurityConfigurationFolder;
import com.sos.joc.model.security.SecurityConfigurationMainEntry;
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
		String iniFileName = Globals.getShiroIniInClassPath();
		ini = Ini.fromResourcePath(Globals.getIniFileForShiro(iniFileName));
		listOfMasters = SOSSecurityConfigurationMasters.getInstance();
	}

	private void addUsers() {

		Section s = getSection(SECTION_USERS);

		for (String user : s.keySet()) {
			SecurityConfigurationUser securityConfigurationUser = new SecurityConfigurationUser();
			SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry(
					s.get(user), null, null);
			securityConfigurationUser.setUser(user);
			securityConfigurationUser.setPassword(sosSecurityConfigurationUserEntry.getPassword());
			securityConfigurationUser.setRoles(sosSecurityConfigurationUserEntry.getRoles());
			securityConfiguration.getUsers().add(securityConfigurationUser);
		}
	}

	private void addMain() throws JocException {

		SOSSecurityConfigurationMainEntry sosSecurityConfigurationMainEntry = new SOSSecurityConfigurationMainEntry();
		HashMap<String, String> comments = new HashMap<String, String>();
		Section mainSection = ini.getSection(SECTION_MAIN);
		if (mainSection == null) {
			JocError jocError = new JocError();
			jocError.setCode("");
			jocError.setMessage("Missing [main] section");
			throw new JocException(jocError);
		}
		for (Map.Entry<String, String> entry : mainSection.entrySet()) {
			if (writeIni.get(SECTION_MAIN).getComment(entry.getKey()) != null) {
				comments.put(entry.getKey(), writeIni.get(SECTION_MAIN).getComment(entry.getKey()));
			}
		}

		for (String main : mainSection.keySet()) {
			List<String> entryComment = sosSecurityConfigurationMainEntry.getMultiLineComment(main, comments);
			List<String> entryValue = sosSecurityConfigurationMainEntry.getMultiLineValue(main, mainSection.get(main));

			SecurityConfigurationMainEntry securityConfigurationMainEntry = new SecurityConfigurationMainEntry();
			securityConfigurationMainEntry.setEntryName(main);
			securityConfigurationMainEntry.setEntryValue(entryValue);

			securityConfigurationMainEntry.setEntryComment(entryComment);
			securityConfiguration.getMain().add(securityConfigurationMainEntry);
		}
	}

	private void addRoles() {

		Section s = getSection(SECTION_ROLES);
		for (String role : s.keySet()) {
			SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = new SOSSecurityConfigurationRoleEntry(
					role, s.get(role));
			sosSecurityConfigurationRoleEntry.addPermissions();
		}
	}

	private void addFolders() {

		Section s = getSection(SECTION_FOLDERS);
		if (s != null) {
			for (String role2Master : s.keySet()) {
				SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = new SOSSecurityConfigurationFolderEntry(
						role2Master, s.get(role2Master));
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
			SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry(
					securityConfigurationUser, oldSection, sosSecurityHashSettings);
			if ((securityConfigurationUser.getPassword() != null && !securityConfigurationUser.getPassword().isEmpty())
					|| securityConfigurationUser.getRoles().size() > 0) {
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
					SOSSecurityConfigurationRoleEntry sosSecurityConfigurationRoleEntry = new SOSSecurityConfigurationRoleEntry(
							role);
					roles.put(role, sosSecurityConfigurationRoleEntry);
				}

				if (folders.get(folderKey) == null) {
					SOSSecurityConfigurationFolderEntry sosSecurityConfigurationFolderEntry = new SOSSecurityConfigurationFolderEntry(
							folderKey);
					folders.put(folderKey, sosSecurityConfigurationFolderEntry);
				}

				for (SecurityConfigurationFolder securityConfigurationFolder : securityConfigurationRole.getFolders()) {
					SOSSecurityFolderItem sosSecurityFolderItem = new SOSSecurityFolderItem(master,
							securityConfigurationFolder);
					folders.get(folderKey).addFolder(sosSecurityFolderItem.getIniValue());
				}
				for (SecurityConfigurationPermission securityConfigurationPermission : securityConfigurationRole
						.getPermissions()) {
					SOSSecurityPermissionItem sosSecurityPermissionItem = new SOSSecurityPermissionItem(master,
							securityConfigurationPermission);
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
				writeIni.get(SECTION_FOLDERS).put(masterAndRole,
						sosSecurityConfigurationFolderEntry.getIniWriteString());
			}
		}
	}

	private boolean roleIsEmpty(String role) {
		for (Map.Entry<String, SecurityConfigurationMaster> entry : listOfMasters.getListOfMasters().entrySet()) {
			SecurityConfigurationMaster securityConfigurationMaster = listOfMasters.getListOfMasters()
					.get(entry.getKey());

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



	private void writeMain() throws InvalidFileFormatException, IOException {
		clearSection(SECTION_MAIN);
		Profile.Section s = writeIni.get(SECTION_MAIN);
		for (SecurityConfigurationMainEntry securityConfigurationMainEntry : securityConfiguration.getMain()) {
			SOSSecurityConfigurationMainEntry sosSecurityConfigurationMainEntry = new SOSSecurityConfigurationMainEntry(
					securityConfigurationMainEntry);

			String comment = "";
			String nl = "\r\n";
			for (int i = 0; i < securityConfigurationMainEntry.getEntryComment().size(); i++) {
				comment = comment + securityConfigurationMainEntry.getEntryComment().get(i) + nl;
				if (i==securityConfigurationMainEntry.getEntryComment().size()-1) {
					nl = "";
				}
			}
			writeIni.get(SECTION_MAIN).putComment(securityConfigurationMainEntry.getEntryName(),comment);
			s.put(securityConfigurationMainEntry.getEntryName(), sosSecurityConfigurationMainEntry.getIniWriteString());
		}
	}

	public SecurityConfiguration readConfiguration()
			throws InvalidFileFormatException, IOException, JocException, SOSHibernateException {
		SOSHibernateSession sosHibernateSession = Globals.createSosHibernateStatelessConnection("Import shiro.ini");

		try {

			SOSShiroIniShare sosShiroIniShare = new SOSShiroIniShare(sosHibernateSession);
			sosShiroIniShare.provideIniFile();

			writeIni = new Wini(Globals.getShiroIniFile().toFile());

			addUsers();
			addRoles();
			addFolders();
			addMain();
			addEmptyRoles();

			listOfMasters.createConfigurations(securityConfiguration);
			return this.securityConfiguration;
		} finally {
			sosHibernateSession.close();
		}
	}

	public SecurityConfiguration writeConfiguration(SecurityConfiguration securityConfiguration)
			throws IOException, SOSHibernateException, JocException {
		writeIni = new Wini(Globals.getShiroIniFile().toFile());
		SOSHibernateSession sosHibernateSession = Globals.createSosHibernateStatelessConnection("Import shiro.ini");

		try {
			this.securityConfiguration = securityConfiguration;
			writeMain();
			writeUsers();
			writeMasters();
			writeIni.store();

			IniSecurityManagerFactory factory = Globals.getShiroIniSecurityManagerFactory();
            SecurityManager securityManager = factory.getInstance();

            SOSShiroIniShare sosShiroIniShare = new SOSShiroIniShare(sosHibernateSession);
			sosShiroIniShare.copyFileToDb(Globals.getShiroIniFile().toFile());

			return this.securityConfiguration;
		} finally {
			Globals.disconnect(sosHibernateSession);
		}
	}
}
