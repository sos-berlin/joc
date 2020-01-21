
package com.sos.joc.job.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobResourceDocumentation {

    @GET
    @Path("documentation")
    public JOCDefaultResponse postDocumentation(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("job") String job);

    @POST
    @Path("documentation/assign")
    public JOCDefaultResponse assignDocu(@HeaderParam("X-Access-Token") String xAccessToken, byte[] filterBytes);

    @POST
    @Path("documentation/unassign")
    public JOCDefaultResponse unassignDocu(@HeaderParam("X-Access-Token") String xAccessToken, byte[] filterBytes);

}
