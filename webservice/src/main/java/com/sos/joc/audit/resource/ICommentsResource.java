
package com.sos.joc.audit.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface ICommentsResource {

    @POST
    @Path("comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postComments(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken) throws Exception;

}
