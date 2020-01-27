package com.sos.joc.xmleditor.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.xmleditor.JocXmlEditor;

public interface ISchemaDownloadResource {

    public static final String PATH = "schema/download";
    public static final String IMPL_PATH = JocXmlEditor.getResourceImplPath(PATH);

    @GET
    @Path(PATH)
    public JOCDefaultResponse process(@HeaderParam("X-Access-Token") final String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("objectType") String objectType, @QueryParam("show") String show,
            @QueryParam("schemaIdentifier") String schemaIdentifier);

}
