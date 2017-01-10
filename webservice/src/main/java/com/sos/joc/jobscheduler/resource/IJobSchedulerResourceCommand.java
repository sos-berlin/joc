
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.commands.JobschedulerCommand;
import com.sos.joc.model.commands.JobschedulerCommands;
  
public interface IJobSchedulerResourceCommand {
  

    @POST
    @Path("command")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerCommand(
            @HeaderParam("access_token") String accessToken, JobschedulerCommand jobschedulerCommand) throws Exception;

    @POST
    @Path("commands")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobschedulerCommands(
            @HeaderParam("access_token") String accessToken, JobschedulerCommands jobschedulerCommands) throws Exception;

}