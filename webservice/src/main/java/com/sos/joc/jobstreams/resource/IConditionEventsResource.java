package com.sos.joc.jobstreams.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IConditionEventsResource {

    @POST
    @Path("eventlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse getEvents(@HeaderParam("X-Access-Token") String accessToken, byte[] conditionEventsFilter);

    @POST
    @Path("event/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse deleteEvent(@HeaderParam("X-Access-Token") String accessToken, byte[] conditionEvent);

    @POST
    @Path("event/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse addEvent(@HeaderParam("X-Access-Token") String accessToken, byte[] conditionEvent);
}
