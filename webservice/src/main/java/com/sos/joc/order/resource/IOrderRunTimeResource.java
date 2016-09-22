
package com.sos.joc.order.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderFilterSchema;

public interface IOrderRunTimeResource {

    @POST
    @Path("run_time")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrderRunTime(@HeaderParam("access_token") String accessToken, OrderFilterSchema orderFilterSchema) throws Exception;

}
