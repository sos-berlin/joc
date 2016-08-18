package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerBody;
import com.sos.joc.response.JOCCockpitResponse;

public interface IJobSchedulerResourceModifyJobScheduler {
    
    @POST
    @Path("jobscheduler/terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postJobschedulerTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

    
    @POST
    @Path("jobscheduler/terminate_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postJobschedulerRestartTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

    
    
    @POST
    @Path("abort")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postJobschedulerAbort(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

    @POST
    @Path("abort_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postJobschedulerRestartAbort(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("pause")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postJobschedulerPause(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("continue")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postJobschedulerContinue(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody) throws Exception;

           
        
}
