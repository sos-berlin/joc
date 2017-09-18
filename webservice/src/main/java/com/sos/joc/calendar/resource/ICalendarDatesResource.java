
package com.sos.joc.calendar.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.calendar.CalendarDatesFilter;

 
public interface ICalendarDatesResource {

    @POST
    @Path("dates")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postCalendarDates(@HeaderParam("X-Access-Token") String xAccessToken, CalendarDatesFilter calendarFilter) throws Exception;
}
