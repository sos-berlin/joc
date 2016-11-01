package com.sos.auth.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.subject.Subject;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.WebserviceConstants;

public class SOSShiroCurrentUser {

    private Subject currentSubject;
    private String username;
    private String password;
    private String accessToken;
    private String authorization;
    private String selectedInstance;

    private SOSPermissionJocCockpit sosPermissionJocCockpit;
    private Map<String, DBItemInventoryInstance> listOfSchedulerInstances;

    public SOSShiroCurrentUser(String username, String password) {
        super();
        this.listOfSchedulerInstances = new HashMap<String, DBItemInventoryInstance>();
        this.username = username;
        this.password = password;
    }

    public SOSPermissionJocCockpit getSosPermissionJocCockpit() {
        return sosPermissionJocCockpit;
    }

    public void setSosPermissionJocCockpit(SOSPermissionJocCockpit sosPermissionJocCockpit) {
        this.sosPermissionJocCockpit = sosPermissionJocCockpit;
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
    
    private boolean getPermissionFromSubject(String permission,String permissionMaster){
        boolean excluded = currentSubject.isPermitted("-" + permission) || currentSubject.isPermitted("-" + permissionMaster);
        return (currentSubject.isPermitted(permission) || currentSubject.isPermitted(permissionMaster))  && !excluded;
    }
    
    private boolean getPermissionFromMaster(String permission){
        if (selectedInstance == null) {
            JOCPreferences jocPreferences = new JOCPreferences(username);
            selectedInstance = jocPreferences.get(WebserviceConstants.SELECTED_INSTANCE, "");
        }
        String permissionMaster = selectedInstance + ":" + permission;
        return getPermissionFromSubject(permission,permissionMaster);
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

    public DBItemInventoryInstance getSchedulerInstanceDBItem(JobSchedulerIdentifier jobSchedulerIdentifier) {
        return listOfSchedulerInstances.get(jobSchedulerIdentifier.getId());
    }

    public void addSchedulerInstanceDBItem(JobSchedulerIdentifier jobSchedulerIdentifier, DBItemInventoryInstance schedulerInstancesDBItem) {
        listOfSchedulerInstances.put(jobSchedulerIdentifier.getId(), schedulerInstancesDBItem);
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

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

}
