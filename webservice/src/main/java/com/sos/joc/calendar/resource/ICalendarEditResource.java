
package com.sos.joc.calendar.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface ICalendarEditResource {

    @POST
    @Path("store")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postStoreCalendar(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarFilterBytes);
    
    @POST
    @Path("save_as")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postSaveAsCalendar(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarFilterBytes);
    
    @POST
    @Path("rename")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postRenameCalendar(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarFilterBytes);
}
