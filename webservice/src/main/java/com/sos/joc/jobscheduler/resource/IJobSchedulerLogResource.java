
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobSchedulerLogResource {

    @GET
    @Path("log")
    @Produces({ MediaType.TEXT_PLAIN })
    public JOCDefaultResponse getMainLog(@HeaderParam("access_token") String accessToken, @QueryParam("accessToken") String queryAccessToken,
            @QueryParam("jobschedulerId") String jobschedulerId) throws Exception;

}
