
package com.sos.joc.processClasses.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.processClass.ProcessClassesFilter;

public interface IProcessClassesResourceP {

    @POST
    @Path("p")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postProcessClassesP(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ProcessClassesFilter processClassFilterSchema) throws Exception;
}
