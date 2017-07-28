
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;

public interface IJobSchedulerLogResource {
    
    @GET
    @Path("log")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse getMainLog(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, @QueryParam("accessToken") String queryAccessToken, @QueryParam("jobschedulerId") String jobschedulerId,
            @QueryParam("host") String host, @QueryParam("port") Integer port) throws Exception;

    @POST
    @Path("log")
    @Consumes("application/json")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse getMainLog(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, HostPortTimeOutParameter hostPortParamSchema) throws Exception;

}
