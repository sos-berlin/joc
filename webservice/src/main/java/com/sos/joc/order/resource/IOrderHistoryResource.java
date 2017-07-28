
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderHistoryFilter;

public interface IOrderHistoryResource {

    @POST
    @Path("history")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrderHistory(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception;
}

 


