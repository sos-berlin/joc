
package com.sos.joc.report.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.report.AgentsFilter;

 
public interface IAgentsResource {

    @POST
    @Path("agents")
    @Produces({ "application/json" })
    public JOCDefaultResponse postAgentsReport(            
            @HeaderParam("X-Access-Token") String xAccessToken, AgentsFilter agentsFilter) throws Exception;
}
