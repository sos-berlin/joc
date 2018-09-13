package com.sos.joc.configurations.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.configuration.ConfigurationsDeleteFilter;
import com.sos.joc.model.configuration.ConfigurationsFilter;

 
public interface IJocConfigurationsResource{

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postConfigurations(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, ConfigurationsFilter configurationsFilter) throws Exception;
    
    @POST
    @Path("delete")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postConfigurationsDelete(@HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, ConfigurationsDeleteFilter configurationsFilter) throws Exception;
 
}
