package com.sos.joc.documentations.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationsFilter;

public interface IDocumentationsExportResource {

    @Path("export")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public JOCDefaultResponse postExportDocumentations(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationsFilter filter) throws Exception;
}
