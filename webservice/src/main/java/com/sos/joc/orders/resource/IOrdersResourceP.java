
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersFilter;

 
public interface IOrdersResourceP {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersP(            
            @HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, OrdersFilter ordersFilter) throws Exception;
}
