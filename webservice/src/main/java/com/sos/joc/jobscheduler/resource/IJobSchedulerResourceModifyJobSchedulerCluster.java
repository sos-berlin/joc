package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobSchedulerResourceModifyJobSchedulerCluster {

    @POST
    @Path("cluster/terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerTerminate(@HeaderParam("X-Access-Token") String accessToken, byte[] timeoutParameter);

    @POST
    @Path("cluster/restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerRestartTerminate(@HeaderParam("X-Access-Token") String accessToken, byte[] timeoutParameter);

    @POST
    @Path("cluster/reactivate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerReactivate(@HeaderParam("X-Access-Token") String accessToken, byte[] timeoutParameter);

    @POST
    @Path("cluster/terminate_failsafe")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerTerminateFailSafe(@HeaderParam("X-Access-Token") String accessToken, byte[] timeoutParameter);
}
