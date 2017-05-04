package com.sos.auth.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;

import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.WebserviceConstants;

public class SOSShiroCurrentUser {

	private static final Logger LOGGER = Logger.getLogger(SOSShiroCurrentUser.class);

	public SOSShiroFolderPermissions getSosShiroFolderPermissions() {
		return sosShiroFolderPermissions;
	}

	private Subject currentSubject;
	private String username;
	private String password;
	private String accessToken;
	private String authorization;
	private String selectedInstance;

	private SOSPermissionJocCockpit sosPermissionJocCockpit;
	private SOSPermissionCommands sosPermissionCommands;
	private Map<String, DBItemInventoryInstance> listOfSchedulerInstances;
	private SOSShiroFolderPermissions sosShiroFolderPermissions;

	public SOSShiroCurrentUser(String username, String password) {
		super();
		initFolders();
		this.listOfSchedulerInstances = new HashMap<String, DBItemInventoryInstance>();
		this.username = username;
		this.password = password;
	}

	public SOSShiroCurrentUser(String username, String password, String authorization) {
		super();
		this.listOfSchedulerInstances = new HashMap<String, DBItemInventoryInstance>();
		this.username = username;
		this.authorization = authorization;
		this.password = password;
	}

	public SOSPermissionJocCockpit getSosPermissionJocCockpit() {
		return sosPermissionJocCockpit;
	}

	public void setSosPermissionJocCockpit(SOSPermissionJocCockpit sosPermissionJocCockpit) {
		this.sosPermissionJocCockpit = sosPermissionJocCockpit;
	}

	public SOSPermissionCommands getSosPermissionCommands() {
		return sosPermissionCommands;
	}

	public void setSosPermissionCommands(SOSPermissionCommands sosPermissionCommands) {
		this.sosPermissionCommands = sosPermissionCommands;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Subject getCurrentSubject() {
		return currentSubject;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setCurrentSubject(Subject currentSubject) {
		this.currentSubject = currentSubject;
	}

	public boolean hasRole(String role) {
		if (currentSubject != null) {
			return currentSubject.hasRole(role);
		} else {
			return false;
		}
	}

	private boolean getPermissionFromSubject(String permission, String permissionMaster) {
		boolean excluded = currentSubject.isPermitted("-" + permission)
				|| currentSubject.isPermitted("-" + permissionMaster);
		return (currentSubject.isPermitted(permission) || currentSubject.isPermitted(permissionMaster)) && !excluded;
	}

	private boolean getPermissionFromMaster(String permission) {
		if (selectedInstance == null) {
			JOCPreferences jocPreferences = new JOCPreferences(username);
			selectedInstance = jocPreferences.get(WebserviceConstants.SELECTED_INSTANCE, "");
		}
		String permissionMaster = selectedInstance + ":" + permission;
		return getPermissionFromSubject(permission, permissionMaster);
	}

	public void setSelectedInstance(String selectedInstance) {
		this.selectedInstance = selectedInstance;
	}

	public boolean isPermitted(String permission) {
		if (currentSubject != null) {
			return getPermissionFromMaster(permission);
		} else {
			return false;
		}
	}

	public boolean isAuthenticated() {
		if (currentSubject != null) {
			return currentSubject.isAuthenticated();
		} else {
			return false;
		}
	}

	public Map<String, DBItemInventoryInstance> getMapOfSchedulerInstances() {
		return listOfSchedulerInstances;
	}

	public DBItemInventoryInstance getSchedulerInstanceDBItem(String jobSchedulerId) {
		return listOfSchedulerInstances.get(jobSchedulerId);
	}

	public DBItemInventoryInstance removeSchedulerInstanceDBItem(String jobSchedulerId) {
		return listOfSchedulerInstances.remove(jobSchedulerId);
	}

	public void addSchedulerInstanceDBItem(String jobSchedulerId, DBItemInventoryInstance schedulerInstancesDBItem) {
		listOfSchedulerInstances.put(jobSchedulerId, schedulerInstancesDBItem);
	}

	public DBItemInventoryInstance getSchedulerInstanceByKey(Long id) {
		for (Map.Entry<String, DBItemInventoryInstance> entry : listOfSchedulerInstances.entrySet()) {
			DBItemInventoryInstance instance = entry.getValue();
			if (instance.getId() == id) {
				return instance;
			}
		}
		return null;

	}

	public void initFolders() {
		sosShiroFolderPermissions = new SOSShiroFolderPermissions();
	}

	public void addFolder(String role, String folders) {
		if (sosShiroFolderPermissions == null) {
			this.initFolders();
		}

		if (hasRole(role)) {
			LOGGER.debug(String.format("Adding folders %s for role %s", folders,role));
			sosShiroFolderPermissions.setFolders(folders);
		}
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

}
