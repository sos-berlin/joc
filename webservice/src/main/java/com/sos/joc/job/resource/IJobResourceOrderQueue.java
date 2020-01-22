
package com.sos.joc.job.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
 
public interface IJobResourceOrderQueue {

    @POST
    @Path("order_queue")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobOrderQueue(@HeaderParam("X-Access-Token") String accessToken, byte[] jobFilterBytes);
}

