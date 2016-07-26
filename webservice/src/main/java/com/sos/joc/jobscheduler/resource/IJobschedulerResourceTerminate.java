package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.jobscheduler.post.JobSchedulerTerminateBody;
import com.sos.joc.response.JocCockpitResponse;

@Path("Ijobscheduler")
public interface IJobschedulerResourceTerminate {
    
    @POST
    @Path("terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postjobschedulerTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerTerminateBody jobSchedulerTerminateBody) throws Exception;

       
}
