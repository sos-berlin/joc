
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;

 
public interface IOrdersResourceOverviewSnapshot {

    @POST
    @Path("overview/snapshot")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersOverviewSnapshot(            
            @HeaderParam("access_token") String accessToken, JobChainsFilterSchema filterSchema) throws Exception;


 
    
}
