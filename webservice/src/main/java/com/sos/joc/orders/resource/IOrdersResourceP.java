
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.post.OrdersBody;

 
public interface IOrdersResourceP {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public OrdersResponseP postOrdersP(            
            @HeaderParam("access_token") String accessToken, OrdersBody orderBody) throws Exception;

    public class OrdersResponseP extends com.sos.joc.support.ResponseWrapper {
        
        private OrdersResponseP(Response delegate) {
            super(delegate);
        }
        
        public static IOrdersResourceP.OrdersResponseP responseStatus200(OrdersPSchema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResourceP.OrdersResponseP(responseBuilder.build());
        }
        
        public static IOrdersResourceP.OrdersResponseP responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResourceP.OrdersResponseP(responseBuilder.build());
        }

        public static IOrdersResourceP.OrdersResponseP responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResourceP.OrdersResponseP(responseBuilder.build());
        }           

        public static IOrdersResourceP.OrdersResponseP responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResourceP.OrdersResponseP(responseBuilder.build());
        }           

        public static IOrdersResourceP.OrdersResponseP responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new IOrdersResourceP.OrdersResponseP(responseBuilder.build());
        }           
    }   
 
    
}
