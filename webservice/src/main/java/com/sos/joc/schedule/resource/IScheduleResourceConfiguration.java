
package com.sos.joc.schedule.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IScheduleResourceConfiguration {

    @POST
    @Path("configuration")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postScheduleConfiguration(@HeaderParam("X-Access-Token") String accessToken, byte[] scheduleConfigurationFilter);

    @POST
    @Path("run_time")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postScheduleRunTime(@HeaderParam("X-Access-Token") String accessToken, byte[] scheduleConfigurationFilterSchema);

}
