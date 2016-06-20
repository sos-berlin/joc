package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.jobscheduler.model.Jobscheduler;
import com.sos.joc.jobscheduler.model.JobschedulerVolatilePart;
import com.sos.joc.jobscheduler.resource.IJobschedulerResource;

@Path("jobscheduler")
public class JobschedulerResourceImpl implements IJobschedulerResource {

   
    @Override
    public GetJobschedulerResponse getJobscheduler(String host, Integer port) throws Exception {
        JobschedulerVolatilePart entity = new JobschedulerVolatilePart();
        entity.setDeliveryDate(new Date());
        Jobscheduler jobscheduler = new Jobscheduler();
        jobscheduler.setHost(host);
        jobscheduler.setJobschedulerId("id");
        jobscheduler.setPort(port);
        entity.setJobscheduler(jobscheduler);
        GetJobschedulerResponse getJobschedulerResponse = GetJobschedulerResponse.responseStatus200(entity);
        return getJobschedulerResponse;
    }

  

    
 
}
