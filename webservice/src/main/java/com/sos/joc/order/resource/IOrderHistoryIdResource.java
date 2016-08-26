
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.order.post.OrderBody;

public interface IOrderHistoryIdResource {

    @POST
    @Path("{historyId}")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrderHistoryId(@HeaderParam("access_token") String accessToken,  @PathParam("historyId") String historyId) throws Exception;

}

 


