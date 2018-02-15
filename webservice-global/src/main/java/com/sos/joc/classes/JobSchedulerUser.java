package com.sos.joc.classes;

import java.util.List;

 
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.auth.rest.SOSShiroSession;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;

public class JobSchedulerUser {

	private String accessToken;
	private SOSShiroCurrentUser sosShiroCurrentUser;

	public JobSchedulerUser(String accessToken) {
		super();
		this.accessToken = accessToken;
	}

	public SOSShiroCurrentUser getSosShiroCurrentUser() throws SessionNotExistException {
		if (sosShiroCurrentUser == null && Globals.jocWebserviceDataContainer.getCurrentUsersList() != null) {
			sosShiroCurrentUser = Globals.jocWebserviceDataContainer.getCurrentUsersList().getUser(accessToken);
		}
		if (sosShiroCurrentUser == null) {
			throw new SessionNotExistException("Session doesn't exist");
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

	public DBItemInventoryInstance getSchedulerInstance(String jobSchedulerId) throws JocException {
		if (getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerId) == null) {
			SOSHibernateSession session = Globals.createSosHibernateStatelessConnection("getSchedulerInstance");
			InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(session);
			Globals.beginTransaction(session);
			getSosShiroCurrentUser().addSchedulerInstanceDBItem(jobSchedulerId,
					dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerId, getAccessToken()));
			Globals.rollback(session);
			session.close();
		}
		return getSosShiroCurrentUser().getSchedulerInstanceDBItem(jobSchedulerId);
	}

	public boolean resetTimeOut() throws SessionNotExistException {

		if (sosShiroCurrentUser != null) {
			SOSShiroSession sosShiroSession = new SOSShiroSession(sosShiroCurrentUser);
			sosShiroSession.touch();
		} else {
			throw new org.apache.shiro.session.InvalidSessionException("Session doesn't exist");
		}

		return (sosShiroCurrentUser != null);
	}

	public void setJocJsonCommands(List<JOCJsonCommand> jocJsonCommands) {
		sosShiroCurrentUser.setJocJsonCommands(jocJsonCommands);	
	}

 
}
