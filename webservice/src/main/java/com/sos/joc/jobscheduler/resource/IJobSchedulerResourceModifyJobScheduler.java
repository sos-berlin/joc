package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobSchedulerResourceModifyJobScheduler {
    
    @POST
    @Path("terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerTerminate(@HeaderParam("X-Access-Token") String accessToken, byte[] urlTimeoutParamSchema);

    
    @POST
    @Path("restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerRestartTerminate(@HeaderParam("X-Access-Token") String accessToken, byte[] urlTimeoutParamSchema);

    
    
    @POST
    @Path("abort")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerAbort(@HeaderParam("X-Access-Token") String accessToken, byte[] urlTimeoutParamSchema);

    @POST
    @Path("abort_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerRestartAbort(@HeaderParam("X-Access-Token") String accessToken, byte[] urlTimeoutParamSchema);

           
    @POST
    @Path("pause")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerPause(@HeaderParam("X-Access-Token") String accessToken, byte[] urlTimeoutParamSchema);

           
    @POST
    @Path("continue")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerContinue(@HeaderParam("X-Access-Token") String accessToken, byte[] urlTimeoutParamSchema);
}
