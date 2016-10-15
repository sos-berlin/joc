package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;
import com.sos.joc.model.jobscheduler.OperatingSystem;
import com.sos.joc.model.jobscheduler.JobSchedulerP;

public class JobSchedulerResourceP extends JOCResourceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);
    private String accessToken;
    private JobSchedulerId jobSchedulerId;

    public void setJobSchedulerFilterSchema(JobSchedulerId jobSchedulerId) {
        this.jobSchedulerId = jobSchedulerId;
    }

    public JobSchedulerResourceP(String accessToken, JobSchedulerId jobSchedulerId) {
        super();
        this.accessToken = accessToken;
        this.jobSchedulerId = jobSchedulerId;
        jobschedulerUser = new JobSchedulerUser(accessToken);
    }

    public JOCDefaultResponse postJobschedulerP() {

        LOGGER.debug("init jobscheduler/p");
        try {
            Globals.beginTransaction();

            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerId.getJobschedulerId(), getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance schedulerSupervisorInstancesDBItem = dbLayer.getInventoryInstancesByKey(supervisorId);
            
            if (schedulerSupervisorInstancesDBItem == null) {
                return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table SCHEDULER_INSTANCES", dbItemInventoryInstance.getSupervisorId()));
            }

            JobSchedulerP200 entity = new JobSchedulerP200();
            entity.setDeliveryDate(new Date());

            // TODO JOC Cockpit Webservice

            JobSchedulerP jobscheduler = new JobSchedulerP();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(jobSchedulerId.getJobschedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartedAt());

            ClusterMemberType clusterMemberTypeSchema = new ClusterMemberType();
            clusterMemberTypeSchema.setPrecedence(-1);
            clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getPrecedence());
            // clusterMemberTypeSchema.setType(ClusterMemberTypeSchema.Type.fromValue(schedulerInstancesDBItem.getClusterMemberType()));
            clusterMemberTypeSchema.set_type(ClusterMemberType._type.STANDALONE);
            jobscheduler.setClusterType(clusterMemberTypeSchema);

            OperatingSystem os = new OperatingSystem();
            // os.setArchitecture(Os.Architecture.fromValue(schedulerInstancesDBItem.getArchitecture()));
            os.setArchitecture("32");
            os.setDistribution("distribution");
            os.setName("osName");
            jobscheduler.setOs(os);

            HostPortParameter supervisor = new HostPortParameter();
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
        }finally{
            Globals.rollback();

        }
    }

}
