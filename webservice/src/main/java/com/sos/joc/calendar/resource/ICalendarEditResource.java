
package com.sos.joc.calendar.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.calendar.CalendarMoveFilter;

 
public interface ICalendarEditResource {

    @POST
    @Path("store")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postStoreCalendar(@HeaderParam("X-Access-Token") String xAccessToken, Calendar calendar) throws Exception;
    
    @POST
    @Path("move")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postMoveCalendar(@HeaderParam("X-Access-Token") String xAccessToken, CalendarMoveFilter calendarFilter) throws Exception;
}
