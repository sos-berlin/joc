package com.sos.joc.jobs.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.ModifyJobsSchema;

public interface IJobsResourceCommandModifyJob {

    @POST
    @Path("stop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsStop(@HeaderParam("access_token") String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception;

    @POST
    @Path("unstop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsUnstop(@HeaderParam("access_token") String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception;

    @POST
    @Path("end_all_tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsEndAllTasks(@HeaderParam("access_token") String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception;

    @POST
    @Path("suspend_all_tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsSuspendAllTasks(@HeaderParam("access_token") String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception;

    @POST
    @Path("continue_all_tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsContinueAllTasks(@HeaderParam("access_token") String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception;

 
    @POST
    @Path("set_run_time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsSetRunTime(@HeaderParam("access_token") String accessToken, ModifyJobsSchema modifyJobsSchema) throws Exception;

}
