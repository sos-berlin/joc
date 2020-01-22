
package com.sos.joc.jobchainnodes.resources;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobChainsResourceModifyJobChainNodes {

    @POST
    @Path("stop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesStop(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyNodes);

    @POST
    @Path("skip")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesSkip(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyNodes);

    @POST
    @Path("activate")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesActivate(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyNodes);

}
