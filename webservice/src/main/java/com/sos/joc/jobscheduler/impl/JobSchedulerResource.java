package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.Jobscheduler200VSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.response.JOCDefaultResponse;
import com.sos.joc.response.JOCCockpitResponse;

public class JobSchedulerResource extends JOCResourceImpl {
    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);
    private String accessToken;

    public JobSchedulerResource(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {
        super();
        this.jobSchedulerDefaultBody = jobSchedulerDefaultBody;
        jobschedulerUser = new JobSchedulerUser(accessToken);
        this.accessToken = accessToken;
    }

    JobSchedulerDefaultBody jobSchedulerDefaultBody;

    public JOCDefaultResponse postJobscheduler() {
        LOGGER.debug("init Jobscheduler");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerDefaultBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Jobscheduler200VSchema entity = new Jobscheduler200VSchema();
            entity.setDeliveryDate(new Date());
            Jobscheduler_ jobscheduler = new Jobscheduler_();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartTime());

            // TODO JOC Cockpit Webservice

            State state = new State();
            state.setSeverity(State.Severity._0);
            state.setText(State.Text.PAUSED);
            jobscheduler.setState(state);
            jobscheduler.setSurveyDate(dbItemInventoryInstance.getModified());
            entity.setJobscheduler(jobscheduler);
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatus420(JOCCockpitResponse.getError420Schema(e.getMessage()));
        }
    }

}
