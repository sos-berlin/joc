package com.sos.joc.tasks.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.ModifyTasksSchema;
 
public interface ITasksResourceKill {

    @POST
    @Path("terminate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksTerminate(@HeaderParam("access_token") String accessToken, ModifyTasksSchema modifyTasksSchema) throws Exception;

    @POST
    @Path("terminate_within")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksTerminateWithin(@HeaderParam("access_token") String accessToken, ModifyTasksSchema modifyTasksSchema) throws Exception;

    @POST
    @Path("kill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksKill(@HeaderParam("access_token") String accessToken, ModifyTasksSchema modifyTasksSchema) throws Exception;

   
}
