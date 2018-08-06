package com.sos.joc.joc.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.annotation.Compress;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.JOClog;

public interface ILogResource {

    @Path("log")
    @POST
    @Compress
    //@Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postLog(@HeaderParam("X-Access-Token") String accessToken, JOClog jocLog);

    @Path("log")
    @GET
    @Compress
    //@Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON })
    public JOCDefaultResponse getLog(@HeaderParam("X-Access-Token") String accessToken, @QueryParam("accessToken") String queryAccessToken, @QueryParam("filename") String filename);
    
    @Path("logs")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postLogs(@HeaderParam("X-Access-Token") String accessToken);
}
