
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface IJobSchedulerResourceP {

    @POST
    @Path("p")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerP(@HeaderParam("X-Access-Token") String accessToken, byte[] jobSchedulerFilterSchema);
    
}
