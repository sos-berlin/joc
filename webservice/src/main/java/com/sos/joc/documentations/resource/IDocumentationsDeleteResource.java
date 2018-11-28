package com.sos.joc.documentations.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationsFilter;

public interface IDocumentationsDeleteResource {

    @POST
    @Path("delete")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse deleteDocumentations(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationsFilter filter) throws Exception;
}
