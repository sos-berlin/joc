
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.order.post.OrderConfigurationBody;
import com.sos.joc.response.JOCDefaultResponse;

public interface IOrderConfigurationResource {

    @POST
    @Path("configuration")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrderConfiguration(@HeaderParam("access_token") String accessToken, OrderConfigurationBody orderBody) throws Exception;

}
