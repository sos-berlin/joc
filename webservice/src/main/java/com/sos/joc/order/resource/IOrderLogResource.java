
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.order.post.OrderBody;

public interface IOrderLogResource {

    @POST
    @Path("log")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrderLog(@HeaderParam("access_token") String accessToken, OrderBody orderBody) throws Exception;

}

 


