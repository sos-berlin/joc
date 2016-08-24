package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.ClusterMemberTypeSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Supervisor;
import com.sos.joc.response.JOCDefaultResponse;

public class JobSchedulerResourceP extends JOCResourceImpl {

    private static final Logger LOGGER = Logger.getLogger(JobSchedulerResource.class);
    private String accessToken;
    private JobSchedulerDefaultBody jobSchedulerDefaultBody;

    public JobSchedulerResourceP(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) {
        super();
        this.accessToken = accessToken;
        this.jobSchedulerDefaultBody = jobSchedulerDefaultBody;
        jobschedulerUser = new JobSchedulerUser(accessToken);
    }

    public JOCDefaultResponse postJobschedulerP() {

        LOGGER.debug("init JobschedulerClusterMembers");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerDefaultBody.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            DBItemInventoryInstance schedulerSupervisorInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(dbItemInventoryInstance
                    .getSupervisorId()));
            if (schedulerSupervisorInstancesDBItem == null) {
                return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES",
                        dbItemInventoryInstance.getSupervisorId()));
            }

            Jobscheduler200PSchema entity = new Jobscheduler200PSchema();
            entity.setDeliveryDate(new Date());

            // TODO JOC Cockpit Webservice

            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerDefaultBody.getJobschedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartTime());

            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getClusterMemberPrecedence());
            // clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.fromValue(schedulerInstancesDBItem.getClusterMemberType()));
            clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.active);
            jobscheduler.setClusterType(clusterMemberTypeSchema);

            Os os = new Os();
            // os.setArchitecture(Os.Architecture.fromValue(schedulerInstancesDBItem.getArchitecture()));
            os.setArchitecture("32");
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
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

}
