package com.sos.joc.jobs.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobsResourceModifyJob {

    @POST
    @Path("stop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsStop(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobsBytes);

    @POST
    @Path("unstop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsUnstop(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobsBytes);

    @POST
    @Path("end_all_tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsEndAllTasks(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobsBytes);

    @POST
    @Path("suspend_all_tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsSuspendAllTasks(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobsBytes);

    @POST
    @Path("continue_all_tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsContinueAllTasks(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobsBytes);

 
    @POST
    @Path("set_run_time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsSetRunTime(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobsBytes);
    

}
