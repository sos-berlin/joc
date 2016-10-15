package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourcePSupervisor;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorPImpl  implements IJobSchedulerResourcePSupervisor {
 

    @Override
    public JOCDefaultResponse postJobschedulerSupervisorP(String accessToken, JobSchedulerId jobSchedulerId) throws Exception {
        JobSchedulerResourceP jobSchedulerPResource = new JobSchedulerResourceP(accessToken, jobSchedulerId);

        Globals.beginTransaction();
        DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerPResource.getJobschedulerUser().getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerId.getJobschedulerId()));  
        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s",jobSchedulerId. getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES));
        }
        InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        Long supervisorId = dbItemInventoryInstance.getSupervisorId();
        dbItemInventoryInstance = dbLayer.getInventoryInstancesByKey(supervisorId);
        
        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId for supervisor of %s with internal id %s not found in table %s",jobSchedulerId. getJobschedulerId(),supervisorId,DBLayer.TABLE_INVENTORY_INSTANCES));
        }
     
        jobSchedulerId.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        jobSchedulerPResource.setJobSchedulerFilterSchema(jobSchedulerId);
        Globals.rollback();
        return jobSchedulerPResource.postJobschedulerP();


    }

}
