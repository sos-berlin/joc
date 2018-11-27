package com.sos.joc.documentations.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.docu.DocumentationsFilter;

public interface IDocumentationsExportResource {

    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM) 
    public JOCDefaultResponse postImportDocumentations(@HeaderParam("X-Access-Token") String xAccessToken, DocumentationsFilter filter) throws Exception;
}
