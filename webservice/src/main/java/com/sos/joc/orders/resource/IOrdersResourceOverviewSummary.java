
package com.sos.joc.orders.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersFilter;

 
public interface IOrdersResourceOverviewSummary {

    @POST
    @Path("overview/summary")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersOverviewSummary(            
            @HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, OrdersFilter ordersFilter) throws Exception;
}
