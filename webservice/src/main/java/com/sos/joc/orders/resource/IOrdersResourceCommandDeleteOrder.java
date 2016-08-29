package com.sos.joc.orders.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.orders.post.commands.modify.ModifyOrdersBody;

public interface IOrdersResourceCommandDeleteOrder {

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersDelete(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBody) throws Exception;

}
