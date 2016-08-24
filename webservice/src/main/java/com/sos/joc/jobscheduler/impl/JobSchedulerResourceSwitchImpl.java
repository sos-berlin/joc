package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSwitch;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceSwitchImpl extends JOCResourceImpl  implements IJobSchedulerResourceSwitch {

    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerSwitch(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
  
        LOGGER.debug("init JobschedulerSwitch");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerDefaultBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JOCPreferences jocPreferences = new JOCPreferences();
            jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, jobSchedulerDefaultBody.getJobschedulerId());
            return JOCDefaultResponse.responseStatusJSOk(new Date());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

}
