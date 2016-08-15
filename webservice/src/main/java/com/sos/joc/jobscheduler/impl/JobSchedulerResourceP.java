package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.ClusterMemberTypeSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Supervisor;
import com.sos.joc.response.JobSchedulerPResponse;
import com.sos.joc.response.JocCockpitResponse;

public class JobSchedulerResourceP extends JOCResourceImpl{
    
    private String accessToken;
    private JobSchedulerDefaultBody jobSchedulerDefaultBody;

    public JobSchedulerResourceP(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {
        super();
        this.accessToken = accessToken;
        this.jobSchedulerDefaultBody = jobSchedulerDefaultBody;
        jobschedulerUser = new JobSchedulerUser(accessToken);
    }
    
    public JobSchedulerPResponse postJobschedulerP (){

        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobSchedulerPResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobSchedulerPResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (!getPermissons().getJobschedulerMaster().getView().isStatus()) {
            return JobSchedulerPResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobSchedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {
            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return JobSchedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s",jobSchedulerIdentifier.getSchedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES)));
            }

            
            DBItemInventoryInstance schedulerSupervisorInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(dbItemInventoryInstance.getSupervisorId()));
            if (schedulerSupervisorInstancesDBItem == null) {
                return JobSchedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",dbItemInventoryInstance.getSupervisorId())));
            }

            
            Jobscheduler200PSchema entity = new Jobscheduler200PSchema();
            entity.setDeliveryDate(new Date());
            
            //TODO JOC Cockpit Webservice

            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartTime());
            
            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getClusterMemberPrecedence());
            //clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.fromValue(schedulerInstancesDBItem.getClusterMemberType()));
            clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.ACTIVE);
            jobscheduler.setClusterType(clusterMemberTypeSchema);
            
            Os os = new Os();
            //os.setArchitecture(Os.Architecture.fromValue(schedulerInstancesDBItem.getArchitecture()));
            os.setArchitecture(Os.Architecture._32);
            os.setDistribution("distribution");
            os.setName("osName");
            jobscheduler.setOs(os);
            
            Supervisor supervisor = new Supervisor();
            supervisor.setHost(schedulerSupervisorInstancesDBItem.getHostname());
            supervisor.setPort(schedulerSupervisorInstancesDBItem.getPort());
            supervisor.setJobschedulerId(schedulerSupervisorInstancesDBItem.getSchedulerId());
            jobscheduler.setSupervisor(supervisor);
            
            jobscheduler.setTimeZone(dbItemInventoryInstance.getTimeZone()); 
            jobscheduler.setVersion(dbItemInventoryInstance.getJobSchedulerVersion());
 
            jobscheduler.setSurveyDate(dbItemInventoryInstance.getModified());
            
            entity.setJobscheduler(jobscheduler);
            JobSchedulerPResponse jobschedulerResponse = JobSchedulerPResponse.responseStatus200(entity);
            return jobschedulerResponse;

        } catch (Exception e) {
            return JobSchedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }
    }

}
