package com.sos.joc.jobscheduler.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Path;
import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.MastersVSchema;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.response.JocCockpitResponse;
import com.sos.xml.SOSXmlCommand;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl  extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {

    @Override
    public JobschedulerClusterMembersResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {

        JobschedulerClusterMembersResponse jobschedulerClusterResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerClusterMembersResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerClusterMembersResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }
        
        if (!getPermissons().getJobschedulerMaster().getView().isStatus()){
            return JobschedulerClusterMembersResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerClusterMembersResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterMembersResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        try {

            DBItemInventoryInstance schedulerInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (schedulerInstancesDBItem == null) {
                return JobschedulerClusterMembersResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",jobSchedulerDefaultBody.getJobschedulerId())));
            }

 
            SOSXmlCommand sosXmlCommand = new SOSXmlCommand(schedulerInstancesDBItem.getUrl());
            sosXmlCommand.excutePost("<show_state subsystems=\"folder\" what=\"folders no_subfolders cluster\" path=\"/does/not/exist\"/>");
            sosXmlCommand.executeXPath("//spooler/answer/state/cluster/cluster_member");
          
            MastersVSchema entity = new MastersVSchema();
             
            //TODO JOC Cockpit Webservice
           
            entity.setDeliveryDate(new Date());
            ArrayList<Jobscheduler_> masters = new ArrayList<Jobscheduler_>();
            Jobscheduler_ jobscheduler = new Jobscheduler_();
            jobscheduler.setHost("myHost");
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(-1);
            jobscheduler.setStartedAt(new Date());
            State state = new State();
            state.setSeverity(Severity._0);
            state.setText(Text.DEAD);
            jobscheduler.setState(state);
            jobscheduler.setSurveyDate(new Date());
            masters.add(jobscheduler);

            Jobscheduler_ jobscheduler2 = new Jobscheduler_();
            jobscheduler2.setHost("mySecondHost");
            jobscheduler2.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler2.setPort(-1);
            jobscheduler2.setStartedAt(new Date());
            State state2 = new State();
            state2.setSeverity(Severity._0);
            state2.setText(Text.RUNNING);
            jobscheduler2.setState(state2);
            jobscheduler2.setSurveyDate(new Date());
            masters.add(jobscheduler2);

            entity.setMasters(masters);

            jobschedulerClusterResponse = JobschedulerClusterMembersResponse.responseStatus200(entity);

            return jobschedulerClusterResponse;
        } catch (Exception e) {
            return JobschedulerClusterMembersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }
 

    

}
