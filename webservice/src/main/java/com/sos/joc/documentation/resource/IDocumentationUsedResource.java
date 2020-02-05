package com.sos.joc.documentation.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IDocumentationUsedResource {

    @Path("used")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public JOCDefaultResponse postDocumentationsUsed(@HeaderParam("X-Access-Token") String xAccessToken, byte[] filterBytes);
    
}
