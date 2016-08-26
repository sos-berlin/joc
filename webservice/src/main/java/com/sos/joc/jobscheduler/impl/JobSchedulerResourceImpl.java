package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResource;

@Path("jobscheduler")
public class JobSchedulerResourceImpl implements IJobSchedulerResource {

    @Override
    public JOCDefaultResponse postJobscheduler(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
        
        JobSchedulerResource jobSchedulerResource = new JobSchedulerResource(accessToken, jobSchedulerDefaultBody);
        return jobSchedulerResource.postJobscheduler();
       
    }

}
