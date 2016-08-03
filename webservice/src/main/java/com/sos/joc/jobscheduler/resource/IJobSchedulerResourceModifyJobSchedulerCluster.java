package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerClusterBody;
import com.sos.joc.response.JocCockpitResponse;

@Path("Ijobscheduler")
public interface IJobSchedulerResourceModifyJobSchedulerCluster {
    
    @POST
    @Path("cluster/terminate")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception;

    
    @POST
    @Path("cluster/terminate_and_restart")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerRestartTerminate(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception;

    
    
    @POST
    @Path("cluster/terminate_failsafe")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JocCockpitResponse postJobschedulerTerminateFailSafe(
            @HeaderParam("access_token") String accessToken, JobSchedulerModifyJobSchedulerClusterBody jobSchedulerClusterTerminateBody) throws Exception;
 
        
}
