package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.ClusterMemberTypeSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;
import com.sos.joc.model.jobscheduler.Os;
import com.sos.joc.model.jobscheduler.Supervisor;

public class JobSchedulerResourceP extends JOCResourceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);
    private String accessToken;
    private JobSchedulerFilterSchema jobSchedulerFilterSchema;

    public void setJobSchedulerFilterSchema(JobSchedulerFilterSchema jobSchedulerFilterSchema) {
        this.jobSchedulerFilterSchema = jobSchedulerFilterSchema;
    }

    public JobSchedulerResourceP(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) {
        super();
        this.accessToken = accessToken;
        this.jobSchedulerFilterSchema = jobSchedulerFilterSchema;
        jobschedulerUser = new JobSchedulerUser(accessToken);
    }

    public JOCDefaultResponse postJobschedulerP() {

        LOGGER.debug("init jobscheduler/p");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            DBItemInventoryInstance schedulerSupervisorInstancesDBItem = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(dbItemInventoryInstance
                    .getSchedulerId()));
            if (schedulerSupervisorInstancesDBItem == null) {
                return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES", dbItemInventoryInstance.getSupervisorId()));
            }

            Jobscheduler200PSchema entity = new Jobscheduler200PSchema();
            entity.setDeliveryDate(new Date());

            // TODO JOC Cockpit Webservice

            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerFilterSchema.getJobschedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartedAt());

            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getPrecedence());
            // clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.fromValue(schedulerInstancesDBItem.getClusterMemberType()));
            clusterMemberTypeSchema.setType("myType");
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
            jobscheduler.setVersion(dbItemInventoryInstance.getVersion());

            jobscheduler.setSurveyDate(dbItemInventoryInstance.getModified());

            entity.setJobscheduler(jobscheduler);
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }

}
