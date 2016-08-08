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
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP;
import com.sos.joc.model.jobscheduler.ClusterMemberTypeSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler;
import com.sos.joc.model.jobscheduler.Jobscheduler_;
import com.sos.joc.model.jobscheduler.MastersPSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Os.Architecture;
import com.sos.joc.model.jobscheduler.State;
import com.sos.joc.model.jobscheduler.State.Severity;
import com.sos.joc.model.jobscheduler.State.Text;
import com.sos.joc.model.jobscheduler.Supervisor;
import com.sos.joc.response.JocCockpitResponse;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersPImpl  extends JOCResourceImpl implements IJobSchedulerResourceClusterMembersP {

    @Override
    public JobschedulerClusterMembersPResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {

        JobschedulerClusterMembersPResponse jobschedulerClusterResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return JobschedulerClusterMembersPResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return JobschedulerClusterMembersPResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }
        
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterMembersPResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (jobSchedulerDefaultBody.getJobschedulerId() == null) {
            return JobschedulerClusterMembersPResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }
        if (!getPermissons().getJobschedulerMasterCluster().getView().isClusterStatus()){
            return JobschedulerClusterMembersPResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        try {

            DBItemInventoryInstance schedulerInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));

            if (schedulerInstancesDBItem == null) {
                return JobschedulerClusterMembersPResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",jobSchedulerDefaultBody.getJobschedulerId())));
            }

 
            MastersPSchema entity = new MastersPSchema();
             
            //TODO JOC Cockpit Webservice
           
            entity.setDeliveryDate(new Date());
            ArrayList<Jobscheduler> masters = new ArrayList<Jobscheduler>();
            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost("myHost");
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(-1);
            jobscheduler.setStartedAt(new Date());
            
            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.ACTIVE);
            jobscheduler.setClusterType(clusterMemberTypeSchema);
            
            Os os = new Os();
            os.setArchitecture(Architecture._64);
            os.setDistribution("myDistribution");
            os.setName("myName");
            jobscheduler.setOs(os);
            
            Supervisor supervisor = new Supervisor();
            supervisor.setHost("myHost");
            supervisor.setJobschedulerId("myJobScheduelerId");
            supervisor.setPort(-1);
            jobscheduler.setSupervisor(supervisor);
            
            jobscheduler.setTimeZone("UTC");
            jobscheduler.setVersion("1.11");
            jobscheduler.setSurveyDate(new Date());
            masters.add(jobscheduler);

            
            Jobscheduler jobscheduler2 = new Jobscheduler();
            jobscheduler2.setHost("myHost2");
            jobscheduler2.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler2.setPort(-1);
            jobscheduler2.setStartedAt(new Date());
            
            ClusterMemberTypeSchema clusterMemberTypeSchema2 = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema2.setPrecedence(-1);
            clusterMemberTypeSchema2.setType(ClusterMemberTypeSchema.Type.ACTIVE);
            jobscheduler.setClusterType(clusterMemberTypeSchema2);
            
            Os os2 = new Os();
            os2.setArchitecture(Architecture._32);
            os2.setDistribution("myDistribution2");
            os2.setName("myName2");
            jobscheduler.setOs(os2);
            
            Supervisor supervisor2 = new Supervisor();
            supervisor2.setHost("myHost2");
            supervisor2.setJobschedulerId("myJobScheduelerId2");
            supervisor2.setPort(-1);
            jobscheduler.setSupervisor(supervisor2);
            
            jobscheduler2.setTimeZone("UTC");
            jobscheduler2.setVersion("1.11");
            jobscheduler2.setSurveyDate(new Date());
            masters.add(jobscheduler2);
          

            entity.setMasters(masters);

            jobschedulerClusterResponse = JobschedulerClusterMembersPResponse.responseStatus200(entity);

            return jobschedulerClusterResponse;
        } catch (Exception e) {
            return JobschedulerClusterMembersPResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }
 

    

}
