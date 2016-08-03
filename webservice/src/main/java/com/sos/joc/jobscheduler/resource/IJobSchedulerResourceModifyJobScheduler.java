package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerBody;
import com.sos.joc.response.JocCockpitResponse;

@Path("Ijobscheduler")
public interface IJobSchedulerResourceModifyJobScheduler {
    
    @POST
    @Path("terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

    
    @POST
    @Path("terminate_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerRestartTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

    
    
    @POST
    @Path("abort")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerAbort(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

    @POST
    @Path("abort_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerRestartAbort(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("pause")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerPause(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("continue")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerContinue(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

           
        
}
