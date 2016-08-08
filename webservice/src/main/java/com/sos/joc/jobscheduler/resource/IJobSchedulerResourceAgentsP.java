
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentsBody;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.jobscheduler.AgentsPSchema;
 
@Path("Ijobscheduler")
public interface IJobSchedulerResourceAgentsP {
 

    @POST
    @Path("agents/p")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse postJobschedulerAgentsP(
            @HeaderParam("access_token") String accessToken, JobSchedulerAgentsBody jobSchedulerAgentsBody) throws Exception;
     
   
    public class JobschedulerAgentsPResponse extends com.sos.joc.support.ResponseWrapper {
           
           private JobschedulerAgentsPResponse(Response delegate) {
               super(delegate);
           }
           
           public static IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse responseStatus200(AgentsPSchema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse(responseBuilder.build());
           }
           
           public static IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse responseStatus420(Error420Schema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse(responseBuilder.build());
           }

           public static IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentsP.JobschedulerAgentsPResponse(responseBuilder.build());
           }           
       }   
    

}
