package com.sos.joc.classes;

import com.sos.auth.classes.SOSShiroProperties;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;


public class SOSJobschedulerUser {

    private String accessToken;
    private boolean wasAuthenticated;
    private SOSShiroCurrentUser sosShiroCurrentUser;

    public SOSJobschedulerUser(String accessToken) {
        super();
        this.wasAuthenticated=false;
        this.accessToken = accessToken;
    }

    public SOSShiroCurrentUser getSosShiroCurrentUser() {
        if (sosShiroCurrentUser == null) {
            sosShiroCurrentUser = SOSServicePermissionShiro.currentUsersList.getUser(accessToken);
        }
        return sosShiroCurrentUser;
    }

    public boolean isAuthenticated() {
        if (sosShiroCurrentUser == null  && SOSServicePermissionShiro.currentUsersList != null) {
            sosShiroCurrentUser = SOSServicePermissionShiro.currentUsersList.getUser(accessToken);
        }
        resetTimeOut();
        wasAuthenticated = wasAuthenticated || (sosShiroCurrentUser != null);

        return (sosShiroCurrentUser != null);
    }

    public boolean isTimedOut() {
        if (sosShiroCurrentUser == null && SOSServicePermissionShiro.currentUsersList != null) {
            sosShiroCurrentUser = SOSServicePermissionShiro.currentUsersList.getUser(accessToken);
        }

        return (wasAuthenticated && (sosShiroCurrentUser == null));
    }
 
    public SchedulerInstancesDBItem getSchedulerInstance(String schedulerId){
        if (getSosShiroCurrentUser().getSchedulerInstanceDBItem(schedulerId) == null) {
            SOSShiroProperties sosShiroProperties = new SOSShiroProperties();
            SchedulerInstancesDBLayer schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(sosShiroProperties.getProperty("hibernate_configuration_file"));
            getSosShiroCurrentUser().addSchedulerInstanceDBItem (schedulerId,schedulerInstancesDBLayer.getFirstInstanceById(schedulerId));
        }
        return getSosShiroCurrentUser().getSchedulerInstanceDBItem(schedulerId);
    }
    
    private void resetTimeOut(){
        if (sosShiroCurrentUser != null){
           sosShiroCurrentUser.getCurrentSubject().getSession().touch();
        }
    }
}