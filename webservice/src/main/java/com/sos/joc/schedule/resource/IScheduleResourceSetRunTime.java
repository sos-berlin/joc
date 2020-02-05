
package com.sos.joc.schedule.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IScheduleResourceSetRunTime {

    @POST
    @Path("set_run_time")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postScheduleSetRuntime(@HeaderParam("X-Access-Token") String accessToken,  byte[] modifyRuntime);

}
