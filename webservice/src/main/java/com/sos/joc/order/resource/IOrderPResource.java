
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.order.post.OrderBody;

public interface IOrderPResource {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrderP(@HeaderParam("access_token") String accessToken, OrderBody orderBody) throws Exception;

}
