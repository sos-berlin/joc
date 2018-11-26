package com.sos.joc.documentations.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationsFilter;

public interface IDocumentationsExportResource {

    @POST
    @Path("export")
    @Consumes("application/json")
    // @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse export(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationsFilter documentationsFilter) throws Exception;
}
