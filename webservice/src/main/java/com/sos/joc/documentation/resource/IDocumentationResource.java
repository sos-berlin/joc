package com.sos.joc.documentation.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationsFilter;

public interface IDocumentationResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postDocumentation(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationsFilter filter) throws Exception;
}
