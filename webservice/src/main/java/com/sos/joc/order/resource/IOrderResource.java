
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.order.post.OrderBody;

public interface IOrderResource {

    @POST
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrder(@HeaderParam("access_token") String accessToken, OrderBody orderBody) throws Exception;

}
