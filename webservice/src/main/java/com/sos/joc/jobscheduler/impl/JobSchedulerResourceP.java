package com.sos.joc.jobscheduler.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.os.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.JocError;
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
            Globals.beginTransaction();
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerFilterSchema.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            Jobscheduler200PSchema entity = new Jobscheduler200PSchema();
            entity.setDeliveryDate(new Date());
            // TODO JOC Cockpit Webservice
            Jobscheduler jobscheduler = new Jobscheduler();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartedAt());
            
            ClusterMemberTypeSchema clusterMemberTypeSchema = new ClusterMemberTypeSchema();
            clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getPrecedence());
            clusterMemberTypeSchema.setType(dbItemInventoryInstance.getClusterType());
            jobscheduler.setClusterType(clusterMemberTypeSchema);

            InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryOperatingSystem osItem = osLayer.getInventoryOperatingSystem(dbItemInventoryInstance.getOsId());
            Os os = new Os();
            os.setArchitecture(osItem.getArchitecture());
            os.setDistribution(osItem.getDistribution());
            os.setName(osItem.getName());
            jobscheduler.setOs(os);

            if (supervisorId != 0) {
                DBItemInventoryInstance schedulerSupervisorInstancesDBItem = dbLayer.getInventoryInstancesByKey(supervisorId);
                if (schedulerSupervisorInstancesDBItem == null) {
                    throw new DBInvalidDataException(String.format("supervisor with Id = %s not found in table INVENTORY_INSTANCES", 
                            dbItemInventoryInstance.getSupervisorId()));
                } else {
                    Supervisor supervisor = new Supervisor();
                    supervisor.setHost(schedulerSupervisorInstancesDBItem.getHostname());
                    supervisor.setPort(schedulerSupervisorInstancesDBItem.getPort());
                    supervisor.setJobschedulerId(schedulerSupervisorInstancesDBItem.getSchedulerId());
                    jobscheduler.setSupervisor(supervisor);

                }
            }

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