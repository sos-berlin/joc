
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sos.joc.jobscheduler.model.ErrorObjectWithDeliveryDate;
import com.sos.joc.jobscheduler.model.FiltersForDateFromAndDateTo;
import com.sos.joc.jobscheduler.model.JobschedulerPermanentPart;
import com.sos.joc.jobscheduler.model.JobschedulerStatistics;
import com.sos.joc.jobscheduler.model.JobschedulerVolatilePart;

@Path("Ijobscheduler")
public interface IJobschedulerResource {

    @GET
    @Produces({ "application/json" })
    public IJobschedulerResource.GetJobschedulerResponse getJobscheduler(            
            @QueryParam("host") String host, 
            @QueryParam("port") Integer port) throws Exception;

 
    public class GetJobschedulerResponse extends com.sos.joc.support.ResponseWrapper {

        private GetJobschedulerResponse(Response delegate) {
            super(delegate);
        }

         
        public static IJobschedulerResource.GetJobschedulerResponse responseStatus200(JobschedulerVolatilePart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new IJobschedulerResource.GetJobschedulerResponse(responseBuilder.build());
        }

    }

  
    
}