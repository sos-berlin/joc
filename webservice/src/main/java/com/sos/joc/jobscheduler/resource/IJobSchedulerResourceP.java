
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;

 
@Path("Ijobscheduler")
public interface IJobSchedulerResourceP {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public IJobSchedulerResourceP.JobschedulerPResponse postJobschedulerP(            
            @HeaderParam("access_token") String accessToken, JobSchedulerDefaultBody jobSchedulerDefaultBody) throws Exception;


 
    public class JobschedulerPResponse extends com.sos.joc.support.ResponseWrapper {
        
        private JobschedulerPResponse(Response delegate) {
            super(delegate);
        }
        
        public static IJobSchedulerResourceP.JobschedulerPResponse responseStatus200(Jobscheduler200PSchema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceP.JobschedulerPResponse(responseBuilder.build());
        }
        
        public static IJobSchedulerResourceP.JobschedulerPResponse responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceP.JobschedulerPResponse(responseBuilder.build());
        }

        public static IJobSchedulerResourceP.JobschedulerPResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceP.JobschedulerPResponse(responseBuilder.build());
        }           

        public static IJobSchedulerResourceP.JobschedulerPResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceP.JobschedulerPResponse(responseBuilder.build());
        }           

        public static IJobSchedulerResourceP.JobschedulerPResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobSchedulerResourceP.JobschedulerPResponse(responseBuilder.build());
        }           
    }   
 

  
    
}
