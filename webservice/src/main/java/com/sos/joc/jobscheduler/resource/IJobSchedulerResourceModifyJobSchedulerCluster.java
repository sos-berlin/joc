package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.UrlTimeoutParamSchema;

public interface IJobSchedulerResourceModifyJobSchedulerCluster {
    
    @POST
    @Path("cluster/terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerTerminate(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

    
    @POST
    @Path("cluster/terminate_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerRestartTerminate(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;

    
    
    @POST
    @Path("cluster/terminate_failsafe")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerTerminateFailSafe(
            @HeaderParam("access_token") String accessToken, UrlTimeoutParamSchema urlTimeoutParamSchema) throws Exception;
 
        
}
