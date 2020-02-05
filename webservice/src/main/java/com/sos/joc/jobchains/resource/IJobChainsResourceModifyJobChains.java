
package com.sos.joc.jobchains.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobChainsResourceModifyJobChains {

    @POST
    @Path("stop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainsStop(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobChains);

    @POST
    @Path("unstop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainsUnStop(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyJobChains);

}
