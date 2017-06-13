package com.sos.auth.rest;

import java.nio.file.Path;
import java.nio.file.Paths;
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
    private DBItemInventoryInstance selectedInstance;

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
        if (selectedInstance != null) {
            sosPermissionJocCockpit.setJobschedulerId(selectedInstance.getSchedulerId());
            if (selectedInstance.getPrecedence() == null) {
                sosPermissionJocCockpit.setPrecedence(-1);
            } else {
                sosPermissionJocCockpit.setPrecedence(selectedInstance.getPrecedence());
            }
        }
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

    private String getPermissionSubString(int pos, String permission) {
        String p = permission.replace(':', '/');
        Path path = Paths.get(p, "");
        for (int i = 0; i < pos; i++) {
            p = path.getParent().toString();
            path = Paths.get(p, "");
        }
        p = p.replace('/', ':');
        return p;
    }

    private boolean getExcluded(String permission, String permissionMaster) {
        String[] permissionToken = permission.split(":");
        boolean excluded = false;
        for (int i = 0; i < permissionToken.length; i++) {
            String s = getPermissionSubString(i, permission);
            if (currentSubject != null) {
                excluded = excluded || currentSubject.isPermitted("-" + s);
            }
        }

        if (!excluded) {
            permissionToken = permissionMaster.split(":");
            for (int i = 1; i < permissionToken.length; i++) {
                String s = getPermissionSubString(i - 1, permissionMaster);
                if (currentSubject != null) {
                    excluded = excluded || currentSubject.isPermitted("-" + s);
                }
            }
        }
        return excluded;
    }

    public boolean testGetExcluded(String permission, String permissionMaster) {
        return getExcluded(permission, permissionMaster);
    }

    private boolean getPermissionFromSubject(String permission, String permissionMaster) {
        boolean excluded = currentSubject.isPermitted("-" + permission) || currentSubject.isPermitted("-" + permissionMaster);
        excluded = getExcluded(permission, permissionMaster);
        return (currentSubject.isPermitted(permission) || currentSubject.isPermitted(permissionMaster)) && !excluded;
    }

    private boolean getPermissionFromMaster(String permission) {
        String master;
        if (selectedInstance == null) {
            JOCPreferences jocPreferences = new JOCPreferences(username);
            master = jocPreferences.get(WebserviceConstants.SELECTED_INSTANCE, "");
        } else {
            master = selectedInstance.getSchedulerId();
        }
        String permissionMaster = master + ":" + permission;
        return getPermissionFromSubject(permission, permissionMaster);
    }

    public void setSelectedInstance(DBItemInventoryInstance selectedInstance) {
        this.selectedInstance = selectedInstance;
    }

    public DBItemInventoryInstance getSelectedInstance() {
        return this.selectedInstance;
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

}
