
package com.sos.joc.calendars.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface ICalendarsImportResource {

    @POST
    @Path("import")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse importCalendars(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarImportFilter);
}
