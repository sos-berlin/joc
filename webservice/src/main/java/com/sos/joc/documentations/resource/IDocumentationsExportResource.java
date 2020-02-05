package com.sos.joc.documentations.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IDocumentationsExportResource {

    @Path("export")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public JOCDefaultResponse getExportDocumentations(@HeaderParam("X-Access-Token") String xAccessToken,
            @QueryParam("accessToken") String accessToken, @QueryParam("jobschedulerId") String jobschedulerId,
            @QueryParam("filename") String filename);

    @Path("export")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public JOCDefaultResponse postExportDocumentations(@HeaderParam("X-Access-Token") String xAccessToken, byte[] filterBytes);

    @Path("export/info")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public JOCDefaultResponse postExportInfo(@HeaderParam("X-Access-Token") String xAccessToken, byte[] filterBytes);
}
