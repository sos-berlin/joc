package com.sos.joc.jobstreams.resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
 
public interface IEditOutConditionsResource {

    @POST
    @Path("out_condition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse editJobOutConditions(@HeaderParam("X-Access-Token") String accessToken, byte[] outConditions);
}
