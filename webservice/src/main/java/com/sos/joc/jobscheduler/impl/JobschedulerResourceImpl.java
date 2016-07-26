package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

 
import com.sos.joc.jobscheduler.resource.IJobschedulerResource;
import com.sos.joc.model.jobscheduler.Jobscheduler200VSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler_;

@Path("jobscheduler")
public class JobschedulerResourceImpl implements IJobschedulerResource {

   
    @Override
    public GetJobschedulerResponse getJobscheduler(String host, Integer port) throws Exception {
        Jobscheduler200VSchema entity = new Jobscheduler200VSchema();
        entity.setDeliveryDate(new Date());
        Jobscheduler_ jobscheduler = new Jobscheduler_();
        jobscheduler.setHost(host);
        jobscheduler.setJobschedulerId("id");
        jobscheduler.setPort(port);
        entity.setJobscheduler(jobscheduler);
        GetJobschedulerResponse getJobschedulerResponse = GetJobschedulerResponse.responseStatus200(entity);
        return getJobschedulerResponse;
    }

  

    
 
}
