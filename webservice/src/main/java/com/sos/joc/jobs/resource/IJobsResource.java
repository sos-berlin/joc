
package com.sos.joc.jobs.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.post.JobsBody;

 
public interface IJobsResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobs(            
            @HeaderParam("access_token") String accessToken, JobsBody jobsBody) throws Exception;


 
    
}
