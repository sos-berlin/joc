package com.sos.joc.joc.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.annotation.Compress;
import com.sos.joc.classes.JOCDefaultResponse;

public interface ILogResource {

    @POST
    @Compress
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse postLog(@HeaderParam("access_token") String accessToken);

    @GET
    @Compress
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse getLog(@HeaderParam("access_token") String accessToken, @QueryParam("accessToken") String queryAccessToken);
}
