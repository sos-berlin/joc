package com.sos.joc.documentation.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationShowFilter;

public interface IDocumentationShowResource {
    @POST
    @Path("show")
    @Consumes("application/json")
    // @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse show(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception;
    
    @POST
    @Path("preview")
    @Consumes("application/json")
    // @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse preview(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception;
    
    @POST
    @Path("url")
    @Consumes("application/json")
    // @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postUrl(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationShowFilter documentationFilter) throws Exception;
}
