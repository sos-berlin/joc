
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.jobscheduler.StatisticsSchema;
 
@Path("Ijobscheduler")
public interface IJobSchedulerResourceStatistics {
          
    @GET
    @Path("statistics")
    @Produces({ MediaType.APPLICATION_JSON  })
    public IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse getJobschedulerStatistics(
            @QueryParam("scheduler_id") String schedulerId, 
            @HeaderParam("access_token") String accessToken) throws Exception;    

    @POST
    @Path("statistics")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON })
    public IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse postJobschedulerStatistics(
            @HeaderParam("access_token") String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;
    
    
 
   
    public class JobschedulerStatisticsResponse extends com.sos.joc.support.ResponseWrapper {
           
           private JobschedulerStatisticsResponse(Response delegate) {
               super(delegate);
           }
           
           public static IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse responseStatus200(StatisticsSchema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse(responseBuilder.build());
           }
           
           public static IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse responseStatus420(Error420Schema entity) {
               Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse(responseBuilder.build());
           }

           public static IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse(responseBuilder.build());
           }           

           public static IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
               Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
               responseBuilder.entity(entity);
               return new IJobSchedulerResourceStatistics.JobschedulerStatisticsResponse(responseBuilder.build());
           }           
       }   
    

}
