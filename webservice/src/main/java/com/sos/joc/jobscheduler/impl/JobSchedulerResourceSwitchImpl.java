package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSwitch;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourceSwitchImpl extends JOCResourceImpl implements IJobSchedulerResourceSwitch {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceSwitchImpl.class);
    private static final String API_CALL = "./jobscheduler/switch";

    @Override
    public JOCDefaultResponse postJobschedulerSwitch(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {

        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JOCPreferences jocPreferences = new JOCPreferences();
            jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, jobSchedulerId.getJobschedulerId());
            
            SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
            jocDefaultResponse = sosServicePermissionShiro.getJocCockpitPermissions(accessToken,jobschedulerUser.getSosShiroCurrentUser().getUsername(),jobschedulerUser.getSosShiroCurrentUser().getPassword());
            
            return jocDefaultResponse;

        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, jobSchedulerId));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, jobSchedulerId));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }
}
