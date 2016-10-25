package com.sos.joc.classes.jobscheduler;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOperatingSystem;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.os.InventoryOperatingSystemsDBLayer;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.model.jobscheduler.ClusterMemberType;
import com.sos.joc.model.jobscheduler.ClusterType;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerP;
import com.sos.joc.model.jobscheduler.OperatingSystem;


public class JobSchedulerPermanent {
   
    public static JobSchedulerP getJobScheduler(DBItemInventoryInstance dbItemInventoryInstance) throws Exception {
        JobSchedulerP  jobscheduler = new JobSchedulerP();
        jobscheduler.setHost(dbItemInventoryInstance.getHostname());
        jobscheduler.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        jobscheduler.setPort(dbItemInventoryInstance.getPort());
        jobscheduler.setStartedAt(dbItemInventoryInstance.getStartedAt());
        jobscheduler.setTimeZone(dbItemInventoryInstance.getTimeZone());
        jobscheduler.setVersion(dbItemInventoryInstance.getVersion());
        jobscheduler.setSurveyDate(dbItemInventoryInstance.getModified());
        
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
        
        Long supervisorId = dbItemInventoryInstance.getSupervisorId();
        if (supervisorId != DBLayer.DEFAULT_ID) {
            InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
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
        return jobscheduler;
    }
}
