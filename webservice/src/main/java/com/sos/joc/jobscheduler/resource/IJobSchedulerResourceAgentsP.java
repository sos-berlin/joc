
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.AgentFilter;
 
public interface IJobSchedulerResourceAgentsP {
 

    @POST
    @Path("agents/p")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerAgentsP(
            @HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, AgentFilter agentFilterSchema) throws Exception;
}
