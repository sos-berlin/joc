package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobschedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceP;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster.JobschedulerClusterResponse;
import com.sos.joc.model.jobscheduler.ClusterMemberTypeSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Supervisor;
import com.sos.joc.response.JocCockpitResponse;
import com.sos.scheduler.db.SchedulerInstancesDBItem;

@Path("jobscheduler")
public class JobSchedulerResourcePImpl extends JOCResourceImpl implements IJobSchedulerResourceP {

    @Override
    public JobschedulerPResponse postJobschedulerP(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
        jobschedulerUser = new JobschedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerPResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerPResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (!getPermissons().getJobschedulerMaster().getView().isStatus()) {
            return JobschedulerPResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {
            SchedulerInstancesDBItem schedulerInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (schedulerInstancesDBItem == null) {
                return JobschedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",jobSchedulerDefaultBody.getJobschedulerId())));
            }

            
            Jobscheduler200PSchema entity = new Jobscheduler200PSchema();
            entity.setDeliveryDate(new Date());
            
            //TODO JOC Cockpit Webservice

            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost(schedulerInstancesDBItem.getHostName());
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(schedulerInstancesDBItem.getTcpPort());
            jobscheduler.setStartedAt(schedulerInstancesDBItem.getStartTime());
            
            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setPrecedence(schedulerInstancesDBItem.getClusterMemberPrecedence());
            //clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.fromValue(schedulerInstancesDBItem.getClusterMemberType()));
            clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.ACTIVE);
            jobscheduler.setClusterType(clusterMemberTypeSchema);
            
            Os os = new Os();
            //os.setArchitecture(Os.Architecture.fromValue(schedulerInstancesDBItem.getArchitecture()));
            os.setArchitecture(Os.Architecture._32);
            os.setDistribution(schedulerInstancesDBItem.getDistribution());
            os.setName(schedulerInstancesDBItem.getOsName());
            jobscheduler.setOs(os);
            
            Supervisor supervisor = new Supervisor();
            supervisor.setHost(schedulerInstancesDBItem.getSupervisorHostName());
            supervisor.setPort(schedulerInstancesDBItem.getSupervisorTcpPort());
            supervisor.setJobschedulerId(schedulerInstancesDBItem.getSupervisorSchedulerId());
            jobscheduler.setSupervisor(supervisor);
            
            jobscheduler.setTimeZone(schedulerInstancesDBItem.getTimeZone()); 
            jobscheduler.setVersion(schedulerInstancesDBItem.getJobSchedulerVersion());
 
            jobscheduler.setSurveyDate(schedulerInstancesDBItem.getModificationDate());
            
            entity.setJobscheduler(jobscheduler);
            JobschedulerPResponse jobschedulerResponse = JobschedulerPResponse.responseStatus200(entity);
            return jobschedulerResponse;

        } catch (Exception e) {
            return JobschedulerPResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }
    }

}
