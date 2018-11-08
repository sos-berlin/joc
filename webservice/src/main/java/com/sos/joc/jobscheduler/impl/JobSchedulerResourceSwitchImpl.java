package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.auth.rest.SOSShiroSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSwitch;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.common.Ok;

@Path("jobscheduler")
public class JobSchedulerResourceSwitchImpl extends JOCResourceImpl implements IJobSchedulerResourceSwitch {

	private static final String API_CALL = "./jobscheduler/switch";
	private static final String SESSION_KEY = "selectedInstance";

	@Override
	public JOCDefaultResponse postJobschedulerSwitch(String xAccessToken, String accessToken,
			JobSchedulerId jobSchedulerId) throws Exception {
		return postJobschedulerSwitch(getAccessToken(xAccessToken, accessToken), jobSchedulerId);
	}

	public JOCDefaultResponse postJobschedulerSwitch(String accessToken, JobSchedulerId jobSchedulerId)
			throws Exception {

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerId, accessToken,
					jobSchedulerId.getJobschedulerId(),
					getPermissonsJocCockpit(jobSchedulerId.getJobschedulerId(), accessToken).getJobschedulerMaster()
							.getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			SOSShiroCurrentUser shiroUser = jobschedulerUser.getSosShiroCurrentUser();
			JOCPreferences jocPreferences = new JOCPreferences(shiroUser.getUsername());
			String selectedInstance = jobSchedulerId.getJobschedulerId();
			jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, selectedInstance);
			SOSShiroSession sosShiroSession = new SOSShiroSession(shiroUser);
			sosShiroSession.setAttribute(SESSION_KEY, selectedInstance);

			shiroUser.removeSchedulerInstanceDBItem(dbItemInventoryInstance.getSchedulerId());

			try {
				Globals.forceClosingHttpClients(shiroUser, accessToken);
			} catch (Exception e) {
			}

			Ok okSchema = new Ok();
			okSchema.setOk(true);
			return JOCDefaultResponse.responseStatus200(okSchema);

		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}
}
