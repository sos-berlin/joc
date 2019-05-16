package com.sos.joc.conditions.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.conditions.ConditionCheck;

public interface ICheckConditionsResource {

    @POST
    @Path("check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse checkCondition(            
            @HeaderParam("X-Access-Token") String accessToken, ConditionCheck conditionCheck) throws Exception;
}
