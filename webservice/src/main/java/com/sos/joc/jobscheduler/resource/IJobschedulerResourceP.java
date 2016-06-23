
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.joc.jobscheduler.model.ErrorObjectWithDeliveryDate;
import com.sos.joc.jobscheduler.model.FiltersForDateFromAndDateTo;
import com.sos.joc.jobscheduler.model.JobschedulerPermanentPart;

@Path("Ijobscheduler")
public interface IJobschedulerResourceP {

    @POST
    @Path("p")
    @Consumes(MediaType.APPLICATION_JSON )
    @Produces({ MediaType.APPLICATION_JSON  })
    public IJobschedulerResourceP.PostJobschedulerPResponse postJobschedulerP(
            @QueryParam("host") String host, 
            @QueryParam("port") Long port, 
            FiltersForDateFromAndDateTo entity) throws Exception;

 
    @GET
    @Path("p")
    @Produces({ MediaType.APPLICATION_JSON  })
    public IJobschedulerResourceP.GetJobschedulerPResponse getJobschedulerPermanent(
            @QueryParam("host") String host, 
            @QueryParam("port") Long port) throws Exception;
    

   
 
    public class GetJobschedulerPResponse extends com.sos.joc.support.ResponseWrapper {

        private GetJobschedulerPResponse(Response delegate) {
            super(delegate);
        }

         
        public static IJobschedulerResourceP.GetJobschedulerPResponse statusCode200(JobschedulerPermanentPart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobschedulerResourceP.GetJobschedulerPResponse(responseBuilder.build());
        }

        
        public static IJobschedulerResourceP.GetJobschedulerPResponse statusCode420(ErrorObjectWithDeliveryDate entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobschedulerResourceP.GetJobschedulerPResponse(responseBuilder.build());
        }

    }

  
    public class PostJobschedulerPResponse extends com.sos.joc.support.ResponseWrapper {

        private PostJobschedulerPResponse(Response delegate) {
            super(delegate);
        }
 
        public static IJobschedulerResourceP.PostJobschedulerPResponse responseStatus200(JobschedulerPermanentPart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobschedulerResourceP.PostJobschedulerPResponse(responseBuilder.build());
        }

         
        public static IJobschedulerResourceP.PostJobschedulerPResponse responseStatus420(ErrorObjectWithDeliveryDate entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IJobschedulerResourceP.PostJobschedulerPResponse(responseBuilder.build());
        }

    }
    
    
}
