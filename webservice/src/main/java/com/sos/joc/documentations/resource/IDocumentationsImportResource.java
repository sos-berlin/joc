package com.sos.joc.documentations.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IDocumentationsImportResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON) 
    public JOCDefaultResponse postImportDocumentations(
            @HeaderParam("X-Access-Token") String xAccessToken,
            @FormDataParam("jobschedulerId") String jobschedulerId, 
            @FormDataParam("folder") String directory, 
            @FormDataParam("file") FormDataBodyPart body) throws Exception;
}
