
package com.sos.joc.schedule.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleFilterSchema;

public interface IScheduleResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSchedule(@HeaderParam("access_token") String accessToken, ScheduleFilterSchema scheduleFilterSchema) throws Exception;

}
