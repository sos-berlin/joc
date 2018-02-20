package com.sos.auth.rest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;

import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.auth.rest.permission.model.SOSPermissionCommandsMaster;
import com.sos.auth.rest.permission.model.SOSPermissionCommandsMasters;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpitMaster;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpitMasters;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCJsonCommand;

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

    private SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters;
    private SOSPermissionCommandsMasters sosPermissionCommandsMasters;
    private Map<String, DBItemInventoryInstance> listOfSchedulerInstances;
    private Map<String, SOSPermissionJocCockpit> listOfSOSPermissionJocCockpit;
    private Map<String, SOSPermissionCommands> listOfSOSPermissionCommands;
    private SOSShiroFolderPermissions sosShiroFolderPermissions;
    private Set<JOCJsonCommand> jocJsonCommands;
    
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

    public SOSPermissionJocCockpit getSosPermissionJocCockpit(String masterId) {
    	if (listOfSOSPermissionJocCockpit == null) {
    		initListOfSOSPermissionJocCockpit();
    	}
    	if(listOfSOSPermissionJocCockpit.containsKey(masterId)) {
            return listOfSOSPermissionJocCockpit.get(masterId);
    	} else {
            return listOfSOSPermissionJocCockpit.get("");
    	}
    }
    
    private void initListOfSOSPermissionJocCockpit() {
    	listOfSOSPermissionJocCockpit = new HashMap<String,SOSPermissionJocCockpit>();
    	for (SOSPermissionJocCockpitMaster permission: sosPermissionJocCockpitMasters.getSOSPermissionJocCockpitMaster()) {
    		listOfSOSPermissionJocCockpit.put(permission.getJobSchedulerMaster(), permission.getSOSPermissionJocCockpit());
    	}
		
	}

    private void initListOfSOSPermissionCommands() {
    	listOfSOSPermissionCommands = new HashMap<String,SOSPermissionCommands>();
    	for (SOSPermissionCommandsMaster permission: sosPermissionCommandsMasters.getSOSPermissionCommandsMaster()) {
    		listOfSOSPermissionCommands.put(permission.getJobSchedulerMaster(), permission.getSOSPermissionCommands());
    	}
		
	}

	public SOSPermissionJocCockpitMasters getSosPermissionJocCockpitMasters() {
        return sosPermissionJocCockpitMasters;
    }

    public void setSosPermissionJocCockpitMasters(SOSPermissionJocCockpitMasters sosPermissionJocCockpitMasters) {
        this.sosPermissionJocCockpitMasters = sosPermissionJocCockpitMasters;
    }

	public SOSPermissionCommandsMasters getSosPermissionCommandsMasters() {
        return sosPermissionCommandsMasters;
    }

    public void setSosPermissionCommandsMasters(SOSPermissionCommandsMasters sosPermissionCommandsMasters) {
        this.sosPermissionCommandsMasters = sosPermissionCommandsMasters;
    }
    
    public SOSPermissionCommands getSosPermissionCommands(String masterId) {
    	if (listOfSOSPermissionCommands == null) {
    		initListOfSOSPermissionCommands();
    	}
        return listOfSOSPermissionCommands.get(masterId);
    }

    public void setSosPermissionCommands(SOSPermissionCommandsMasters sosPermissionCommands) {
        this.sosPermissionCommandsMasters = sosPermissionCommands;
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

    private boolean getExcluded(String permission, String masterId) {
        boolean excluded = false;
        if (currentSubject != null) {
            Path path = Paths.get(permission.replace(':', '/'));
            int nameCount = path.getNameCount();
            for (int i = 0; i < nameCount - 1; i++) {
                if (excluded) {
                    break;
                }
                String s = path.subpath(0, nameCount - i).toString().replace(File.separatorChar, ':');
                excluded = currentSubject.isPermitted("-" + s) || currentSubject.isPermitted("-" + masterId + ":" + s);
            }
        }
        return excluded;
    }

    public boolean testGetExcluded(String permission, String masterId) {
        return getExcluded(permission, masterId);
    }

    private boolean getPermissionFromSubject(String masterId, String permission) {
        return (currentSubject.isPermitted(permission) || currentSubject.isPermitted(masterId + ":" + permission)) && !getExcluded(permission, masterId);
    }
     
    public boolean isPermitted(String masterId, String permission) {
        if (currentSubject != null) {
            return getPermissionFromSubject(masterId, permission);
        } else {
            return false;
        }
    }

    public boolean isPermitted(String permission) {
        if (currentSubject != null) {
            return getPermissionFromSubject("", permission);
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

        String jobSchedulerId = "";
        if (role.contains("|")) {
            String[] s = role.split("\\|");
            if (s.length > 1) {
                jobSchedulerId = s[0];
                role = s[1];
            }
        }

        if (hasRole(role)) {
            LOGGER.debug(String.format("Adding folders %s for role %s", folders, role));
            sosShiroFolderPermissions.setFolders(jobSchedulerId, folders);
        }
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

	public Set<JOCJsonCommand> getJocJsonCommands() {
		return jocJsonCommands;
	}

	public void setJocJsonCommands(Set<JOCJsonCommand> jocJsonCommands) {
		this.jocJsonCommands = jocJsonCommands;
	}

}
