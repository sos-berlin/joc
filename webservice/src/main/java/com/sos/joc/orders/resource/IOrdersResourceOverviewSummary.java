
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.orders.post.orders.OrdersBody;

 
public interface IOrdersResourceOverviewSummary {

    @POST
    @Path("overview/summary")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersOverviewSummary(            
            @HeaderParam("access_token") String accessToken, OrdersBody ordersBody) throws Exception;


 
    
}
