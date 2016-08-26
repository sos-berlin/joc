
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.orders.post.orders.OrdersBody;

 
public interface IOrdersResource {

    @POST
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrders(            
            @HeaderParam("access_token") String accessToken, OrdersBody orderBody) throws Exception;


 
    
}
