
package com.sos.joc.job.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobCalendarsResource {

    @POST
    @Path("calendars")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobCalendars(@HeaderParam("X-Access-Token") String xAccessToken, byte[] jobFilterBytes);

}
