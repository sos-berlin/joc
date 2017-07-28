
package com.sos.joc.event.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.event.custom.CheckEvent;

 
public interface ICheckCustomEventResource {

    @POST
    @Path("custom/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse checkEvent(            
            @HeaderParam("X-Access-Token") String accessToken, CheckEvent checkEvent) throws Exception;
}
