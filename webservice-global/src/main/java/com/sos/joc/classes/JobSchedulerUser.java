package com.sos.joc.classes;

import org.apache.shiro.session.Session;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;

public class JobSchedulerUser {

    private String accessToken;
    private SOSShiroCurrentUser sosShiroCurrentUser;
 
    public JobSchedulerUser(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public SOSShiroCurrentUser getSosShiroCurrentUser() {
        if (sosShiroCurrentUser == null && Globals.jocWebserviceDataContainer.getCurrentUsersList() != null) {
            sosShiroCurrentUser = Globals.jocWebserviceDataContainer.getCurrentUsersList().getUser(accessToken);
        }
        return sosShiroCurrentUser;
    }

    public boolean isAuthenticated() {
        if (sosShiroCurrentUser == null && Globals.jocWebserviceDataContainer.getCurrentUsersList() != null) {
            sosShiroCurrentUser = Globals.jocWebserviceDataContainer.getCurrentUsersList().getUser(accessToken);
        }
        return (sosShiroCurrentUser != null);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public DBItemInventoryInstance getSchedulerInstance(JobSchedulerIdentifier jobSchedulerIdentifier) throws JocException  {
        if (getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier) == null) {
            SOSHibernateConnection connection = Globals.createSosHibernateStatelessConnection("getSchedulerInstance");
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
            Globals.beginTransaction(connection);
            getSosShiroCurrentUser().addSchedulerInstanceDBItem(jobSchedulerIdentifier, dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerIdentifier.getSchedulerId(), getAccessToken()));
            Globals.rollback(connection);
        }
        return getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier);
    }

    public boolean resetTimeOut() {
        if (sosShiroCurrentUser != null) {
            Session curSession = sosShiroCurrentUser.getCurrentSubject().getSession(false);
            if (curSession != null) {
                curSession.touch();
            } else {
                throw new org.apache.shiro.session.InvalidSessionException("Session doesn't exist");
            }
        }
        return (sosShiroCurrentUser != null);
    }
}
