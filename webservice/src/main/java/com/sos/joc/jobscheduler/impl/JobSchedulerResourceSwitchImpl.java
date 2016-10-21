package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSwitch;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourceSwitchImpl extends JOCResourceImpl implements IJobSchedulerResourceSwitch {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceSwitchImpl.class);
    private static final String API_CALL = "API-CALL: ./jobscheduler/switch";

    @Override
    public JOCDefaultResponse postJobschedulerSwitch(String accessToken, JobSchedulerId jobSchedulerFilterSchema) throws Exception {

        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JOCPreferences jocPreferences = new JOCPreferences();
            jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, jobSchedulerFilterSchema.getJobschedulerId());
            return JOCDefaultResponse.responseStatusJSOk(new Date());

        } catch (JocException e) {
            e.addErrorMetaInfo(API_CALL, "USER: "+getJobschedulerUser().getSosShiroCurrentUser().getUsername());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }
}
