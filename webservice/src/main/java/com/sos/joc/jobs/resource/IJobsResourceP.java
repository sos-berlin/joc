
package com.sos.joc.jobs.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.post.JobsBody;


public interface IJobsResourceP {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobsP(            
            @HeaderParam("access_token") String accessToken, JobsBody jobsBody) throws Exception;

   
 
    
}

