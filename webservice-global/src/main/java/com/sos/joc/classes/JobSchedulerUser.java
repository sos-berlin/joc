package com.sos.joc.classes;

import org.apache.shiro.session.Session;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;

public class JobSchedulerUser {

    private String accessToken;
    private SOSShiroCurrentUser sosShiroCurrentUser;

    public JobSchedulerUser(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public SOSShiroCurrentUser getSosShiroCurrentUser() {
        if (sosShiroCurrentUser == null && Globals.currentUsersList != null) {
            sosShiroCurrentUser = Globals.currentUsersList.getUser(accessToken);
        }
        return sosShiroCurrentUser;
    }

    public boolean isAuthenticated() {
        if (sosShiroCurrentUser == null && Globals.currentUsersList != null) {
            sosShiroCurrentUser = Globals.currentUsersList.getUser(accessToken);
        }
        return (sosShiroCurrentUser != null);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public DBItemInventoryInstance getSchedulerInstance(JobSchedulerIdentifier jobSchedulerIdentifier) throws DBInvalidDataException, DBMissingDataException, DBConnectionRefusedException {
        if (getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerIdentifier) == null) {
            Globals.beginTransaction();
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            getSosShiroCurrentUser().addSchedulerInstanceDBItem(jobSchedulerIdentifier, dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerIdentifier.getSchedulerId(), getAccessToken()));
            Globals.rollback();
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
