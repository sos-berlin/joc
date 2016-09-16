package com.sos.joc.classes;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;

public class JobSchedulerUser {

    private String accessToken;
    private SOSShiroCurrentUser sosShiroCurrentUser;

    public JobSchedulerUser(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public SOSShiroCurrentUser getSosShiroCurrentUser() {
        if (sosShiroCurrentUser == null) {
            sosShiroCurrentUser = Globals.currentUsersList.getUser(accessToken);
        }
        return sosShiroCurrentUser;
    }

    public boolean isAuthenticated() {
        if (sosShiroCurrentUser == null && Globals.currentUsersList != null) {
            sosShiroCurrentUser = Globals.currentUsersList.getUser(accessToken);
        }
        resetTimeOut();

        return (sosShiroCurrentUser != null);
    }

 

    public String getAccessToken() {
        return accessToken;
    }

    public DBItemInventoryInstance getSchedulerInstance(JobSchedulerIdentifier jobSchedulerIdentifier) throws Exception {
        if (getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier) == null) {
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            getSosShiroCurrentUser().addSchedulerInstanceDBItem(jobSchedulerIdentifier, dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerIdentifier.getSchedulerId()));
        }
        return getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier);
    }

    private void resetTimeOut() {
        if (sosShiroCurrentUser != null) {
            sosShiroCurrentUser.getCurrentSubject().getSession().touch();
        }
    }
}
