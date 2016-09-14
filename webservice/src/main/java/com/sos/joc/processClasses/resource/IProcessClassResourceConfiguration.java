
package com.sos.joc.processClasses.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.processClass.ProcessClassConfigurationFilterSchema;
 
public interface IProcessClassResourceConfiguration {

    @POST
    @Path("configuration")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postProcessClassConfiguration(            
            @HeaderParam("access_token") String accessToken, ProcessClassConfigurationFilterSchema processClassConfigurationFilterSchema) throws Exception;

   
 
    
}

