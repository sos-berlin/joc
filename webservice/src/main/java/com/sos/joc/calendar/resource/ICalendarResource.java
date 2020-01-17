
package com.sos.joc.calendar.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface ICalendarResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postCalendar(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarFilterBytes);
}
