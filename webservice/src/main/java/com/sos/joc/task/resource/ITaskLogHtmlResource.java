
package com.sos.joc.task.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface ITaskLogHtmlResource {

    @GET
    @Path("log/html")
    @Produces({ MediaType.TEXT_HTML})
    public JOCDefaultResponse getTaskLogHtml(@HeaderParam("access_token") String accessToken, @QueryParam("accessToken") String queryAccessToken, @QueryParam("jobschedulerId")  String jobschedulerId,
            @QueryParam("taskId") String taskId) throws Exception;

}

 


