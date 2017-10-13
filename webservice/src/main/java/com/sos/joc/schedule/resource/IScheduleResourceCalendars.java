
package com.sos.joc.schedule.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleFilter;

public interface IScheduleResourceCalendars {

    @POST
    @Path("calendars")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postScheduleCalendars(@HeaderParam("X-Access-Token") String xAccessToken, ScheduleFilter scheduleFilterSchema) throws Exception;

}
