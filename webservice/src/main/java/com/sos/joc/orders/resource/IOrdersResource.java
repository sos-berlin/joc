
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.orders.post.OrdersBody;

 
public interface IOrdersResource {

    @POST
    @Produces({ "application/json" })
    public OrdersResponse postOrders(            
            @HeaderParam("access_token") String accessToken, OrdersBody orderBody) throws Exception;

    public class OrdersResponse extends com.sos.joc.support.ResponseWrapper {
        
        private OrdersResponse(Response delegate) {
            super(delegate);
        }
        
        public static IOrdersResource.OrdersResponse responseStatus200(OrdersVSchema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResource.OrdersResponse(responseBuilder.build());
        }
        
        public static IOrdersResource.OrdersResponse responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResource.OrdersResponse(responseBuilder.build());
        }

        public static IOrdersResource.OrdersResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResource.OrdersResponse(responseBuilder.build());
        }           

        public static IOrdersResource.OrdersResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResource.OrdersResponse(responseBuilder.build());
        }           

        public static IOrdersResource.OrdersResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResource.OrdersResponse(responseBuilder.build());
        }           
    }   
 
    
}
