package com.sos.joc.xmleditor.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.model.xmleditor.xml2json.Xml2JsonConfiguration;

public interface IXml2JsonResource {

    public static final String PATH = "xml2json";
    public static final String IMPL_PATH = JocXmlEditor.getResourceImplPath(PATH);

    @POST
    @Path(PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse xml2json(@HeaderParam("X-Access-Token") final String accessToken, Xml2JsonConfiguration in);

}
