package com.sos.joc.xmleditor.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.xmleditor.validate.ValidateConfiguration;
import com.sos.joc.classes.xmleditor.JocXmlEditor;

public interface IValidateResource {

    public static final String PATH = "validate";
    public static final String IMPL_PATH = JocXmlEditor.getResourceImplPath(PATH);

    @POST
    @Path(PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse validate(@HeaderParam("X-Access-Token") final String accessToken, ValidateConfiguration in);

}