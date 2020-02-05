
package com.sos.joc.yade.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;


 
public interface IYadeOrdersResource {

    @POST
    @Path("orders")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrders(@HeaderParam("X-Access-Token") String xAccessToken, byte[] ordersBody);

}
