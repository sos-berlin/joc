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
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerP;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;
import com.sos.joc.model.jobscheduler.OperatingSystem;

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
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerId.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            // TODO JOC Cockpit Webservice
            JobSchedulerP jobscheduler = new JobSchedulerP();
            jobscheduler.setHost(dbItemInventoryInstance.getHostname());
            jobscheduler.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
            jobscheduler.setPort(dbItemInventoryInstance.getPort());
            jobscheduler.setStartedAt(dbItemInventoryInstance.getStartedAt());
            
            ClusterMemberType clusterMemberTypeSchema = new ClusterMemberType();
            clusterMemberTypeSchema.setPrecedence(dbItemInventoryInstance.getPrecedence());
            clusterMemberTypeSchema.set_type(ClusterType.fromValue(dbItemInventoryInstance.getClusterType()));
            jobscheduler.setClusterType(clusterMemberTypeSchema);

            InventoryOperatingSystemsDBLayer osLayer = new InventoryOperatingSystemsDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryOperatingSystem osItem = osLayer.getInventoryOperatingSystem(dbItemInventoryInstance.getOsId());
            OperatingSystem os = new OperatingSystem();
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
                    HostPortParameter supervisor = new HostPortParameter();
                    supervisor.setHost(schedulerSupervisorInstancesDBItem.getHostname());
                    supervisor.setPort(schedulerSupervisorInstancesDBItem.getPort());
                    supervisor.setJobschedulerId(schedulerSupervisorInstancesDBItem.getSchedulerId());
                    jobscheduler.setSupervisor(supervisor);

                }
            }

            jobscheduler.setTimeZone(dbItemInventoryInstance.getTimeZone());
            jobscheduler.setVersion(dbItemInventoryInstance.getVersion());
            jobscheduler.setSurveyDate(dbItemInventoryInstance.getModified());
            
            JobSchedulerP200 entity = new JobSchedulerP200();
            entity.setDeliveryDate(new Date());
            entity.setJobscheduler(jobscheduler);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }finally{
            Globals.rollback();

        }
    }

}