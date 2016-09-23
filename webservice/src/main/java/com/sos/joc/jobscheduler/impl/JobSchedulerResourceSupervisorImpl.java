package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSupervisor;
import com.sos.joc.model.common.JobSchedulerFilterSchema;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorImpl  implements IJobSchedulerResourceSupervisor {


    @Override
    public JOCDefaultResponse postJobschedulerSupervisor(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) throws Exception {
        JobSchedulerResource jobSchedulerResource = new JobSchedulerResource(accessToken, jobSchedulerFilterSchema);

        DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerResource.getJobschedulerUser().getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerFilterSchema.getJobschedulerId()));  
        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s",jobSchedulerFilterSchema. getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES));
        }
        InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        dbItemInventoryInstance = dbLayer.getInventoryInstancesByKey(dbItemInventoryInstance.getSupervisorId());
     
        jobSchedulerFilterSchema.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        jobSchedulerResource.setJobSchedulerFilterSchema(jobSchedulerFilterSchema);

        return jobSchedulerResource.postJobscheduler();


    }

}
