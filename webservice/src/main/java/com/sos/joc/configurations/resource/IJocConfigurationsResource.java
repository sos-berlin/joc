package com.sos.joc.configurations.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface IJocConfigurationsResource{

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postConfigurations(@HeaderParam("X-Access-Token") String accessToken, byte[] configurationsFilterBytes);
    
    @POST
    @Path("delete")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postConfigurationsDelete(@HeaderParam("X-Access-Token") String accessToken, byte[] configurationsFilterBytes);
 
}
