package com.sos.joc.conditions.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.conditions.OutConditions;

public interface IEditOutConditionsResource {

    @POST
    @Path("out_condition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse editJobOutConditions(            
            @HeaderParam("X-Access-Token") String accessToken, OutConditions outConditions) throws Exception;
}