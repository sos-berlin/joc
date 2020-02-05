
package com.sos.joc.security.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface ISecurityConfigurationResourceRead
{

    @POST
    @Path("read")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSecurityConfigurationRead(@HeaderParam("X-Access-Token") String accessToken);
    
    @POST
    @Path("write")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSecurityConfigurationWrite(@HeaderParam("X-Access-Token") String accessToken, byte[] securityConfiguration);

}
