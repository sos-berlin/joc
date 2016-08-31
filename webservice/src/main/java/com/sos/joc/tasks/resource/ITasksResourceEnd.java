package com.sos.joc.tasks.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.tasks.post.kill.TasksKillBody;
 
public interface ITasksResourceEnd {

     
    @POST
    @Path("end")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksEnd(@HeaderParam("access_token") String accessToken, TasksKillBody tasksKillBody) throws Exception;

}
