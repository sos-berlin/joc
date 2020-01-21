
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
  
public interface IJobSchedulerResourceAgentClustersP {
 

    @POST
    @Path("agent_clusters/p")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerAgentClustersP(@HeaderParam("X-Access-Token") String accessToken, byte[] jobSchedulerAgentClustersBody);
}
