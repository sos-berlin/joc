package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler200VSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.State;

public class JobSchedulerResource extends JOCResourceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);
    private String accessToken;

    public JobSchedulerResource(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) {
        super();
        this.jobSchedulerFilterSchema = jobSchedulerFilterSchema;
        jobschedulerUser = new JobSchedulerUser(accessToken);
        this.accessToken = accessToken;
    }

    JobSchedulerFilterSchema jobSchedulerFilterSchema;

    public JOCDefaultResponse postJobscheduler() {
        LOGGER.debug("init Jobscheduler");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Jobscheduler200VSchema entity = new Jobscheduler200VSchema();
            entity.setDeliveryDate(new Date());
            Jobscheduler_ jobscheduler = new Jobscheduler_();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerFilterSchema.getJobschedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartTime());

            // TODO JOC Cockpit Webservice

            State state = new State();
            state.setSeverity(0);
            state.setText(State.Text.PAUSED);
            jobscheduler.setState(state);
            jobscheduler.setSurveyDate(dbItemInventoryInstance.getModified());
            entity.setJobscheduler(jobscheduler);
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

}
