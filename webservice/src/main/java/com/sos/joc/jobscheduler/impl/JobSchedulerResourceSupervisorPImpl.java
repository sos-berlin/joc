package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourcePSupervisor;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourceSupervisorPImpl  implements IJobSchedulerResourcePSupervisor {
 

    @Override
    public JOCDefaultResponse postJobschedulerSupervisorP(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
        JobSchedulerResourceP jobSchedulerPResource = new JobSchedulerResourceP(accessToken, jobSchedulerDefaultBody);

        DBItemInventoryInstance dbItemInventoryInstance = jobSchedulerPResource.getJobschedulerUser().getSchedulerInstance(new JobSchedulerIdentifier(jobSchedulerDefaultBody.getJobschedulerId()));  
        if (dbItemInventoryInstance == null) {
            return JOCDefaultResponse.responseStatusJSError(String.format("schedulerId %s not found in table %s",jobSchedulerDefaultBody. getJobschedulerId(),DBLayer.TABLE_INVENTORY_INSTANCES));
        }

        jobSchedulerDefaultBody.setJobschedulerId(dbItemInventoryInstance.getSupervisorId());
        return jobSchedulerPResource.postJobschedulerP();


    }

}
