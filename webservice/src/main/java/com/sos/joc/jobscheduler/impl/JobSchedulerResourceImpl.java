package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResource;
import com.sos.joc.response.JobschedulerResponse;

@Path("jobscheduler")
public class JobSchedulerResourceImpl implements IJobSchedulerResource {

    @Override
    public JobschedulerResponse postJobscheduler(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
        
        JobSchedulerResource jobSchedulerResource = new JobSchedulerResource(accessToken, jobSchedulerDefaultBody);
        return jobSchedulerResource.postJobscheduler();
       
    }

}
