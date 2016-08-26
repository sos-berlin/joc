
package com.sos.joc.jobs.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.post.JobsBody;

 
public interface IJobsResource {

    @POST
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobs(            
            @HeaderParam("access_token") String accessToken, JobsBody jobsBody) throws Exception;


 
    
}
