
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.response.JOCDefaultResponse;
 
public interface IJobSchedulerResourceStatistics {
          
    @GET
    @Path("statistics")
    @Produces({ MediaType.APPLICATION_JSON  })
    public JOCDefaultResponse getJobschedulerStatistics(
            @QueryParam("scheduler_id") String schedulerId, 
            @HeaderParam("access_token") String accessToken) throws Exception;    

    @POST
    @Path("statistics")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerStatistics(
            @HeaderParam("access_token") String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;
     
 
    

}
