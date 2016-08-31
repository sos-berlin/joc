package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceSupervisor;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorImpl  implements IJobSchedulerResourceSupervisor {


    @Override
    public JOCDefaultResponse postJobschedulerSupervisor(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
        JobSchedulerResource jobSchedulerResource = new JobSchedulerResource(accessToken, jobSchedulerDefaultBody);

        DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerResource.getJobschedulerUser().getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));  
        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s",jobSchedulerDefaultBody. getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES));
        }
     
        jobSchedulerDefaultBody.setJobschedulerId(dbItemInventoryInstance.getSupervisorId());
        return jobSchedulerResource.postJobscheduler();


    }

}