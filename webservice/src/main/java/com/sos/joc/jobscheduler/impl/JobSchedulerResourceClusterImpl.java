package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobschedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse;
import com.sos.joc.model.jobscheduler.Cluster;
import com.sos.joc.model.jobscheduler.ClusterSchema;
import com.sos.joc.response.JocCockpitResponse;
import com.sos.scheduler.db.SchedulerInstancesDBItem;

@Path("jobscheduler")
public class JobSchedulerResourceClusterImpl  extends JOCResourceImpl implements IJobSchedulerResourceCluster {

    @Override
    public JobschedulerClusterResponse postJobschedulerCluster(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {

        JobschedulerClusterResponse jobschedulerClusterResponse;
        jobschedulerUser = new JobschedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerClusterResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerClusterResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }
        
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        try {

            SchedulerInstancesDBItem schedulerInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (schedulerInstancesDBItem == null) {
                return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",jobSchedulerDefaultBody.getJobschedulerId())));
            }

 
            ClusterSchema entity = new ClusterSchema();
            
            Cluster cluster = new Cluster();
            cluster.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());

            //TODO JOC Cockpit Webservice
            cluster.setSurveyDate(new Date());
            cluster.setType(Cluster.Type.ACTIVE);
 
            entity.setDeliveryDate(new Date());
            entity.setCluster(cluster);

            jobschedulerClusterResponse = JobschedulerClusterResponse.responseStatus200(entity);

            return jobschedulerClusterResponse;
        } catch (Exception e) {
            return JobschedulerClusterResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }
 

    

}
