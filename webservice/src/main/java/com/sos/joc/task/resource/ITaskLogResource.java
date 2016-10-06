
package com.sos.joc.task.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.TaskFilterSchema;

public interface ITaskLogResource {

    @POST
    @Path("log")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTaskLog(@HeaderParam("access_token") String accessToken, TaskFilterSchema taskFilterSchema) throws Exception;

}

 


