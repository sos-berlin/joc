package com.sos.joc.documentation.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IDocumentationShowResource {

    @GET
    @Path("show")
    public JOCDefaultResponse show(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("path") String path, @QueryParam("type") String type);

    @POST
    @Path("show")
    @Consumes("application/json")
    public JOCDefaultResponse show(@HeaderParam("X-Access-Token") String xAccessToken, byte[] documentationFilterBytes);

    @GET
    @Path("preview")
    public JOCDefaultResponse preview(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("documentation") String path);
    
    @POST
    @Path("preview")
    @Consumes("application/json")
    public JOCDefaultResponse preview(@HeaderParam("X-Access-Token") String xAccessToken, byte[] documentationFilterBytes);

    @POST
    @Path("url")
    @Consumes("application/json")
    public JOCDefaultResponse postUrl(@HeaderParam("X-Access-Token") String xAccessToken, byte[] documentationFilterBytes);
}
