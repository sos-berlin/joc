
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.response.JobSchedulerPResponse;

 
@Path("Ijobscheduler")
public interface IJobSchedulerResourceP {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public JobSchedulerPResponse postJobschedulerP(            
            @HeaderParam("access_token") String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;

 
    
}
