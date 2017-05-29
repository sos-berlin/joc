package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.apache.shiro.session.InvalidSessionException;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.SessionNotExistException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSwitch;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourceSwitchImpl extends JOCResourceImpl implements IJobSchedulerResourceSwitch {

    private static final String API_CALL = "./jobscheduler/switch";

    @Override
    public JOCDefaultResponse postJobschedulerSwitch(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobSchedulerId, accessToken, jobSchedulerId.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            SOSShiroCurrentUser shiroUser = jobschedulerUser.getSosShiroCurrentUser();
            JOCPreferences jocPreferences = new JOCPreferences(shiroUser.getUsername());
            String selectedInstance = jobSchedulerId.getJobschedulerId();
            jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, selectedInstance);
            shiroUser.setSelectedInstance(dbItemInventoryInstance);

            SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
            
            try {
                shiroUser.removeSchedulerInstanceDBItem(dbItemInventoryInstance.getSchedulerId());
            } catch (InvalidSessionException e1) {
                throw new SessionNotExistException(e1);
            }

            jocDefaultResponse = sosServicePermissionShiro.getJocCockpitPermissions(accessToken, shiroUser.getUsername(), shiroUser.getPassword());

            try {
                Globals.forceClosingHttpClients(shiroUser.getCurrentSubject().getSession(false));
            } catch (Exception e) {
            }

            return jocDefaultResponse;

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
