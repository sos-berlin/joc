package com.sos.joc.documentation.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationShowFilter;

public interface IDocumentationShowResource {

    @GET
    @Path("show")
    public JOCDefaultResponse show(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("path") String path, @QueryParam("type") String type) throws Exception;

    @POST
    @Path("show")
    @Consumes("application/json")
    public JOCDefaultResponse show(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception;

    @GET
    @Path("preview")
    public JOCDefaultResponse preview(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("documentation") String path) throws Exception;
    
    @POST
    @Path("preview")
    @Consumes("application/json")
    public JOCDefaultResponse preview(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationShowFilter documentationFilter)
            throws Exception;

    @POST
    @Path("url")
    @Consumes("application/json")
    public JOCDefaultResponse postUrl(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationShowFilter documentationFilter)
            throws Exception;
}
