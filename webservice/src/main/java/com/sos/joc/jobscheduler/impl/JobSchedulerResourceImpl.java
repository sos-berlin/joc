package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResource;
import com.sos.joc.model.common.JobSchedulerFilterSchema;

@Path("jobscheduler")
public class JobSchedulerResourceImpl implements IJobSchedulerResource {

    @Override
    public JOCDefaultResponse postJobscheduler(String accessToken, JobSchedulerFilterSchema jobSchedulerFilterSchema) throws Exception {
        
        JobSchedulerResource jobSchedulerResource = new JobSchedulerResource(accessToken, jobSchedulerFilterSchema);
        return jobSchedulerResource.postJobscheduler();
       
    }

}
