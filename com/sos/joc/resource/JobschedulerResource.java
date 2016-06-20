
package com.sos.joc.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import com.sos.joc.model.ErrorObjectWithDeliveryDate;
import com.sos.joc.model.FiltersForDateFromAndDateTo;
import com.sos.joc.model.JobschedulerPermanentPart;
import com.sos.joc.model.JobschedulerVolatilePart;

@Path("jobscheduler")
public interface JobschedulerResource {


    /**
     * 
     */
    @GET
    @Produces({
        "application/json"
    })
    JobschedulerResource.GetJobschedulerResponse getJobscheduler()
        throws Exception
    ;

    /**
     * My Description
     * 
     * @param port
     *     JobScheduler Master port e.g. 4444
     * @param host
     *     JobScheduler Master host e.g. localhost
     * @param entity
     *     
     */
    @POST
    @Path("p")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    JobschedulerResource.PostJobschedulerPResponse postJobschedulerP(
        @QueryParam("host")
        String host,
        @QueryParam("port")
        Long port, FiltersForDateFromAndDateTo entity)
        throws Exception
    ;

    /**
     * 
     * @param port
     *     JobScheduler Master port e.g. 4444
     * @param host
     *     JobScheduler Master host e.g. localhost
     */
    @GET
    @Path("p")
    @Produces({
        "application/json"
    })
    JobschedulerResource.GetJobschedulerPResponse getJobschedulerP(
        @QueryParam("host")
        String host,
        @QueryParam("port")
        Long port)
        throws Exception
    ;

    public class GetJobschedulerPResponse
        extends com.sos.joc.support.ResponseWrapper
    {


        private GetJobschedulerPResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static JobschedulerResource.GetJobschedulerPResponse withJsonOK(JobschedulerPermanentPart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new JobschedulerResource.GetJobschedulerPResponse(responseBuilder.build());
        }

        /**
         * 
         * @param entity
         *     
         */
        public static JobschedulerResource.GetJobschedulerPResponse withJsonMethodFailure(ErrorObjectWithDeliveryDate entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new JobschedulerResource.GetJobschedulerPResponse(responseBuilder.build());
        }

    }

    public class GetJobschedulerResponse
        extends com.sos.joc.support.ResponseWrapper
    {


        private GetJobschedulerResponse(Response delegate) {
            super(delegate);
        }

        /**
         * 
         * @param entity
         *     
         */
        public static JobschedulerResource.GetJobschedulerResponse withJsonOK(JobschedulerVolatilePart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new JobschedulerResource.GetJobschedulerResponse(responseBuilder.build());
        }

    }

    public class PostJobschedulerPResponse
        extends com.sos.joc.support.ResponseWrapper
    {


        private PostJobschedulerPResponse(Response delegate) {
            super(delegate);
        }

        /**
         * Response for 200
         * 
         * @param entity
         *     
         */
        public static JobschedulerResource.PostJobschedulerPResponse withJsonOK(JobschedulerPermanentPart entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new JobschedulerResource.PostJobschedulerPResponse(responseBuilder.build());
        }

        /**
         * 
         * @param entity
         *     
         */
        public static JobschedulerResource.PostJobschedulerPResponse withJsonMethodFailure(ErrorObjectWithDeliveryDate entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", "application/json");
            responseBuilder.entity(entity);
            return new JobschedulerResource.PostJobschedulerPResponse(responseBuilder.build());
        }

    }

}
