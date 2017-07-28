package com.sos.joc.orders.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.ModifyOrders;

public interface IOrdersResourceCommandAddOrder {

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersAdd(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, ModifyOrders modifyOrders) throws Exception;
}
