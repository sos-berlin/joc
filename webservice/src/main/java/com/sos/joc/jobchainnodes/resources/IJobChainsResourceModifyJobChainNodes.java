
package com.sos.joc.jobchainnodes.resources;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.ModifyJobChainNodes;

public interface IJobChainsResourceModifyJobChainNodes {

    @POST
    @Path("stop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesStop(@HeaderParam("X-Access-Token") String xAccessToken,
            @HeaderParam("access_token") String accessToken, ModifyJobChainNodes modifyNodes) throws Exception;

    @POST
    @Path("skip")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesSkip(@HeaderParam("X-Access-Token") String xAccessToken,
            @HeaderParam("access_token") String accessToken, ModifyJobChainNodes modifyNodes) throws Exception;

    @POST
    @Path("activate")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesActivate(@HeaderParam("X-Access-Token") String xAccessToken,
            @HeaderParam("access_token") String accessToken, ModifyJobChainNodes modifyNodes) throws Exception;

}
