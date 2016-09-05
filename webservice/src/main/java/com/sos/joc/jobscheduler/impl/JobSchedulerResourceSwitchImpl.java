package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCPreferences;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSwitch;
import com.sos.joc.model.common.JobSchedulerFilterSchema;

@Path("jobscheduler")
public class JobSchedulerResourceSwitchImpl extends JOCResourceImpl  implements IJobSchedulerResourceSwitch {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);

    @Override
    public JOCDefaultResponse postJobschedulerSwitch(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) throws Exception {
  
        LOGGER.debug("init JobschedulerSwitch");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JOCPreferences jocPreferences = new JOCPreferences();
            jocPreferences.put(WebserviceConstants.SELECTED_INSTANCE, jobSchedulerFilterSchema.getJobschedulerId());
            return JOCDefaultResponse.responseStatusJSOk(new Date());

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

}
