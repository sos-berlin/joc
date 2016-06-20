
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sos.joc.jobscheduler.model.JobschedulerStatistics;
import com.sos.joc.jobscheduler.post.JobSchedulerStatisticsBody;
import com.sos.joc.jobscheduler.resource.IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse;

@Path("Ijobscheduler")
public interface IJobschedulerResourceStatistics {

   

    @GET
    @Path("statistics")
    @Produces({ "application/json" })
    public IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse getJobschedulerStatistics(
            @QueryParam("host") String host, 
            @QueryParam("port") Long port,
            @QueryParam("access_token") String accessToken) throws Exception;    

    @POST
    @Path("statistics")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse postJobschedulerStatistics(JobSchedulerStatisticsBody jobSchedulerStatisticsBody) throws Exception;
    
    public class GetJobschedulerStatisticsResponse extends com.sos.joc.support.ResponseWrapper {
           
           private GetJobschedulerStatisticsResponse(Response delegate) {
               super(delegate);
           }
           
           public static IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse responseStatus200(JobschedulerStatistics entity) {
               Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
               responseBuilder.entity(entity);
               return new IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse(responseBuilder.build());
           }
           
           public static IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse responseStatus420(JobschedulerStatistics entity) {
               Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", "application/json");
               responseBuilder.entity(entity);
               return new IJobschedulerResourceStatistics.GetJobschedulerStatisticsResponse(responseBuilder.build());
           }
           
       }   
    

}
