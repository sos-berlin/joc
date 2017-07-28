
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.commands.JobschedulerCommands;
  
public interface IJobSchedulerResourceCommand {
  

 
    @POST
    @Path("commands")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public JOCDefaultResponse postJobschedulerCommands(
            @HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, JobschedulerCommands jobschedulerCommands) throws Exception;

}
