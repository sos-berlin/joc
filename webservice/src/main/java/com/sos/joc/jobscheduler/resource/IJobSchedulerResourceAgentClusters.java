
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
import com.sos.joc.model.jobscheduler.AgentClustersVSchema;
 
public interface IJobSchedulerResourceAgentClusters {
 

    @POST
    @Path("agent_clusters")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse postJobschedulerAgentClusters(
            @HeaderParam("access_token") String accessToken, JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody) throws Exception;
    
 
    public class JobschedulerAgentClustersResponse extends com.sos.joc.support.ResponseWrapper {
           
           private JobschedulerAgentClustersResponse(Response delegate) {
               super(delegate);
           }
           
           public static IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse responseStatus200(AgentClustersVSchema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse(responseBuilder.build());
           }
           
           public static IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse responseStatus420(Error420Schema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse(responseBuilder.build());
           }

           public static IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse(responseBuilder.build());
           }           
       }   
    

}
