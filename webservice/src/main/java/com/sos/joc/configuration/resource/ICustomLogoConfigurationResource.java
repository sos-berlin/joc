package com.sos.joc.configuration.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface ICustomLogoConfigurationResource{

    @POST
    @Path("logo")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postCustomLogo();

    @GET
    @Path("logo")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse getCustomLogo();
}
