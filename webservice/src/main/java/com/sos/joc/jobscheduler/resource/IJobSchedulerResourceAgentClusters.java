
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
 
public interface IJobSchedulerResourceAgentClusters {
 

    @POST
    @Path("agent_clusters")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerAgentClusters(
            @HeaderParam("access_token") String accessToken, JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody) throws Exception;
    
 
 

}