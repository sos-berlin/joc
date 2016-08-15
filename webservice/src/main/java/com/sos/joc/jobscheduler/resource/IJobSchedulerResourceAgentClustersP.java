
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.jobscheduler.AgentClustersPSchema;
 
public interface IJobSchedulerResourceAgentClustersP {
 

    @POST
    @Path("agent_clusters/p")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse postJobschedulerAgentClusters(
            @HeaderParam("access_token") String accessToken, JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody) throws Exception;
    
 
    public class JobschedulerAgentClustersPResponse extends com.sos.joc.support.ResponseWrapper {
           
           private JobschedulerAgentClustersPResponse(Response delegate) {
               super(delegate);
           }
           
           public static IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse responseStatus200(AgentClustersPSchema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse(responseBuilder.build());
           }
           
           public static IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse responseStatus420(Error420Schema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse(responseBuilder.build());
           }

           public static IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse(responseBuilder.build());
           }           
       }   
    

}
