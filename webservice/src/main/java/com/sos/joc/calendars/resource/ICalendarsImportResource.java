
package com.sos.joc.calendars.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.calendar.CalendarImportFilter;

 
public interface ICalendarsImportResource {

    @POST
    @Path("import")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse importCalendars(@HeaderParam("X-Access-Token") String xAccessToken, CalendarImportFilter calendarImportFilter) throws Exception;
//    public JOCDefaultResponse importCalendars(@HeaderParam("X-Access-Token") String xAccessToken, @FormDataParam("file") InputStream uploadedInputStream,
//            @FormDataParam("file") FormDataContentDisposition fileDetail, @FormParam("jobschedulerId") String jobschedulerId) throws Exception;
}
