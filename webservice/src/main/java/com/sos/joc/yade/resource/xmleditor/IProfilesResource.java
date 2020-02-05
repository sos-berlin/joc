package com.sos.joc.yade.resource.xmleditor;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.yade.JocYade;
import com.sos.joc.model.yade.xmleditor.Profiles;

public interface IProfilesResource {

    public static final String PATH = "profiles";
    public static final String IMPL_PATH = JocYade.getResourceImplPath(PATH);

    @POST
    @Path(PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse getProfiles(@HeaderParam("X-Access-Token") final String accessToken, final Profiles in);

}
