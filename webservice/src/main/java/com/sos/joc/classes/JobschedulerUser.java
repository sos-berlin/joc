package com.sos.joc.classes;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.classes.SOSShiroProperties;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;


public class JobschedulerUser {

    private String accessToken;
    private boolean wasAuthenticated;
    private SOSShiroCurrentUser sosShiroCurrentUser;

    public JobschedulerUser(String accessToken) {
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
 
    public String getAccessToken() {
        return accessToken;
    }

    public SchedulerInstancesDBItem getSchedulerInstance(JobSchedulerIdentifier jobSchedulerIdentifier){
        if (getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier) == null) {
            SOSShiroProperties sosShiroProperties = new SOSShiroProperties();
            SchedulerInstancesDBLayer schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(sosShiroCurrentUser.getSosHibernateDBLayer().getConnection());
            getSosShiroCurrentUser().addSchedulerInstanceDBItem (jobSchedulerIdentifier,schedulerInstancesDBLayer.getInstance(jobSchedulerIdentifier.getSchedulerId(),jobSchedulerIdentifier.getHost(),jobSchedulerIdentifier.getPort()));
        }
        return getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier);
    }
    
    private void resetTimeOut(){
        if (sosShiroCurrentUser != null){
           sosShiroCurrentUser.getCurrentSubject().getSession().touch();
        }
    }
}