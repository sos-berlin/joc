package com.sos.joc.tasks.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.ModifyTasks;
 
public interface ITasksResourceKill {

    @POST
    @Path("terminate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksTerminate(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ModifyTasks modifyTasksSchema) throws Exception;

    @POST
    @Path("terminate_within")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksTerminateWithin(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ModifyTasks modifyTasksSchema) throws Exception;

    @POST
    @Path("kill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksKill(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ModifyTasks modifyTasksSchema) throws Exception;

    @POST
    @Path("end")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksEnd(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ModifyTasks modifyTasksSchema) throws Exception;

}
