package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import javax.ws.rs.Path;

import com.sos.joc.jobscheduler.model.FiltersForDateFromAndDateTo;
import com.sos.joc.jobscheduler.model.Jobscheduler;
import com.sos.joc.jobscheduler.model.JobschedulerVolatilePart;
import com.sos.joc.jobscheduler.resource.IJobschedulerResource;
import com.sos.joc.jobscheduler.resource.IJobschedulerResourceP;

@Path("jobscheduler")
public class JobschedulerPResourceImpl implements IJobschedulerResourceP {

    @Override
    public PostJobschedulerPResponse postJobschedulerP(String host, Long port, FiltersForDateFromAndDateTo entity) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public GetJobschedulerPResponse getJobschedulerPermanent(String host, Long port) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

   
 

    
 
}
