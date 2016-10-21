package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourceStatisticsImpl extends JOCResourceImpl implements IJobSchedulerResourceStatistics {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResourceStatisticsImpl.class);

    @Override
    public JOCDefaultResponse postJobschedulerStatistics(String accessToken, JobSchedulerId jobSchedulerFilterSchema) throws Exception {

        LOGGER.debug("init jobscheduler/statistics");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            //TODO
            
            return JOCDefaultResponse.responseStatus200(null);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }

    }

}
