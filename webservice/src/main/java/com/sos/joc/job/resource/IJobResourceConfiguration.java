
package com.sos.joc.job.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobConfigurationFilterSchema;

public interface IJobResourceConfiguration {

    @POST
    @Path("configuration")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobConfiguration(            
            @HeaderParam("access_token") String accessToken, JobConfigurationFilterSchema jobConfigurationFilterSchema) throws Exception;

   
 
    
}

