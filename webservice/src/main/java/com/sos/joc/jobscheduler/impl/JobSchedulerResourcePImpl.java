package com.sos.joc.jobscheduler.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceP;
import com.sos.joc.model.common.JobSchedulerId;

@Path("jobscheduler")
public class JobSchedulerResourcePImpl extends JOCResourceImpl implements IJobSchedulerResourceP {

    @Override
    public JOCDefaultResponse postJobschedulerP(String accessToken, JobSchedulerId jobSchedulerFilterSchema) throws Exception {
        JobSchedulerResourceP jobSchedulerPResource = new JobSchedulerResourceP(accessToken, jobSchedulerFilterSchema);
        return jobSchedulerPResource.postJobschedulerP();
    }

}
