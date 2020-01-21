package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobSchedulerCleanUpInventoryResource {
    
    @POST
    @Path("cleanup")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerCleanupInventory(@HeaderParam("X-Access-Token") String accessToken, byte[] hostPortParameter);

}
