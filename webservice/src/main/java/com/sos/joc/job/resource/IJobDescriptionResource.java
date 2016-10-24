
package com.sos.joc.job.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobDescriptionResource {

    @GET
    @Path("description")
    @Produces({ MediaType.TEXT_HTML })
    public JOCDefaultResponse getJobDescription(@HeaderParam("access_token") String accessToken, @QueryParam("jobschedulerId") String jobschedulerId,
            @QueryParam("job") String job) throws Exception;

}
