
package com.sos.joc.calendars.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

 
public interface ICalendarsResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postCalendars(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarsFilter);
    
    @POST
    @Path("used")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postUsedBy(@HeaderParam("X-Access-Token") String xAccessToken, byte[] calendarsFilter);
}
