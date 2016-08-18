package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceP;
import com.sos.joc.response.JOCDefaultResponse;

@Path("jobscheduler")
public class JobSchedulerResourcePImpl extends JOCResourceImpl implements IJobSchedulerResourceP {

    @Override
    public JOCDefaultResponse postJobschedulerP(String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception {
        JobSchedulerResourceP jobSchedulerPResource = new JobSchedulerResourceP(accessToken, jobSchedulerDefaultBody);
        return jobSchedulerPResource.postJobschedulerP();
    }

}
