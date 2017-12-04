
package com.sos.joc.calendars.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.calendar.CalendarsFilter;

 
public interface ICalendarsExportResource {

    @POST
    @Path("export")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse exportCalendars(@HeaderParam("X-Access-Token") String xAccessToken, CalendarsFilter calendarsFilter) throws Exception;
}
