
package com.sos.joc.plan.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
 
 
public interface IPlanResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postPlan(@HeaderParam("X-Access-Token") String accessToken,  byte[] planFilter);
}
