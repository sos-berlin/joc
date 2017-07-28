
package com.sos.joc.event.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.event.RegisterEvent;

 
public interface IEventResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postEvent(            
            @HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, RegisterEvent eventBody) throws Exception;
}
