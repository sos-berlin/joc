
package com.sos.joc.jobchain.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainConfigurationFilterSchema;

public interface IJobChainResourceConfiguration {

    @POST
    @Path("configuration")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainConfiguration(            
            @HeaderParam("access_token") String accessToken, JobChainConfigurationFilterSchema jobChainConfigurationFilterSchema) throws Exception;

   
 
    
}

