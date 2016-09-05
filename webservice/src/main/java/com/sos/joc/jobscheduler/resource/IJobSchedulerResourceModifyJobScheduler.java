package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.UrlTimeoutParamSchema;

public interface IJobSchedulerResourceModifyJobScheduler {
    
    @POST
    @Path("terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerTerminate(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

    
    @POST
    @Path("terminate_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerRestartTerminate(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

    
    
    @POST
    @Path("abort")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerAbort(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

    @POST
    @Path("abort_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerRestartAbort(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

           
    @POST
    @Path("pause")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerPause(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

           
    @POST
    @Path("continue")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerContinue(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;
    
          
        
}
