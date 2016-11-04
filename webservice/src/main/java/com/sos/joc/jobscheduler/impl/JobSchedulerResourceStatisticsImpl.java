package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourceStatisticsImpl extends JOCResourceImpl implements IJobSchedulerResourceStatistics {

    private static final String API_CALL = "./jobscheduler/statistics";

    @Override
    public JOCDefaultResponse postJobschedulerStatistics(String accessToken, JobSchedulerId jobSchedulerFilter) throws Exception {

        try {
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            // TODO

            return JOCDefaultResponse.responseStatus200(null);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
