package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.Jobscheduler200VSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.response.JobSchedulerResponse;
import com.sos.joc.response.JocCockpitResponse;

public class JobSchedulerResource extends JOCResourceImpl{
    
    public JobSchedulerResource(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {
        super();
        this.accessToken = accessToken;
        this.jobSchedulerDefaultBody = jobSchedulerDefaultBody;
        jobschedulerUser = new JobSchedulerUser(accessToken);
    }

    private String accessToken;
    JobSchedulerDefaultBody jobSchedulerDefaultBody;
    
    public JobSchedulerResponse postJobscheduler (){

        if (jobschedulerUser.isTimedOut()) {
            return JobSchedulerResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobSchedulerResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (!getPermissons().getJobschedulerMaster().getView().isStatus()) {
            return JobSchedulerResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobSchedulerResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {
            DBItemInventoryInstance schedulerInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (schedulerInstancesDBItem == null) {
                return JobSchedulerResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",jobSchedulerDefaultBody.getJobschedulerId())));
            }

            Jobscheduler200VSchema entity = new Jobscheduler200VSchema();
            entity.setDeliveryDate(new Date());
            Jobscheduler_ jobscheduler = new Jobscheduler_();
            jobscheduler.setHost(schedulerInstancesDBItem.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(schedulerInstancesDBItem.getPort());
            jobscheduler.setStartedAt(schedulerInstancesDBItem.getStartTime());
            
            //TODO JOC Cockpit Webservice
            
            State state = new State();
            state.setSeverity(State.Severity._0);
            state.setText(State.Text.PAUSED);
            jobscheduler.setState(state);
            jobscheduler.setSurveyDate(schedulerInstancesDBItem.getModified());
            entity.setJobscheduler(jobscheduler);
            JobSchedulerResponse jobschedulerResponse = JobSchedulerResponse.responseStatus200(entity);
            return jobschedulerResponse;

        } catch (Exception e) {
            return JobSchedulerResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }
    }

}
