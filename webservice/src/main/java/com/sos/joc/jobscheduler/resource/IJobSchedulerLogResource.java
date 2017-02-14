
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;

public interface IJobSchedulerLogResource {

    @POST
    @Path("log")
    @Consumes("application/json")
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse getMainLog(@HeaderParam("access_token") String accessToken, HostPortTimeOutParameter hostPortParamSchema) throws Exception;

}
