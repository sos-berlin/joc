
package com.sos.joc.job.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobFilter;
 
public interface IJobResourceP {

    @POST
    @Path("p")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobP(            
            @HeaderParam("X-Access-Token") String xAccessToken, @HeaderParam("access_token") String accessToken, JobFilter jobFilter) throws Exception;
}

