package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
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
            JOCPreferences jocPreferences = new JOCPreferences(jobschedulerUser.getSosShiroCurrentUser().getUsername());
            String selectedInstance = jobSchedulerId.getJobschedulerId();
            jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, selectedInstance);
            jobschedulerUser.getSosShiroCurrentUser().setSelectedInstance(selectedInstance);

            SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
            jocDefaultResponse = sosServicePermissionShiro.getJocCockpitPermissions(accessToken, jobschedulerUser.getSosShiroCurrentUser()
                    .getUsername(), jobschedulerUser.getSosShiroCurrentUser().getPassword());

            return jocDefaultResponse;

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
