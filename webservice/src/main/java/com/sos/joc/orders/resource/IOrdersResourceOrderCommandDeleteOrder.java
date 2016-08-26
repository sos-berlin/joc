package com.sos.joc.orders.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.orders.post.commands.start.OrdersModifyOrderBody;

public interface IOrdersResourceOrderCommandDeleteOrder {

    @POST
    @Path("delete")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersDelete(@HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

}
