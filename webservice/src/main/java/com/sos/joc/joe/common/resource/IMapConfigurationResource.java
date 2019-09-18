package com.sos.joc.joe.common.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IMapConfigurationResource {
    
    @POST
    @Path("toJson")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse toJson(@HeaderParam("X-Access-Token") final String accessToken, final byte[] requestBody);
    

}
