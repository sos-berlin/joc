
package com.sos.joc.jobchainnodes.resources;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.ModifyNodeSchema;

public interface IJobChainsResourceModifyJobChainNodes {

    @POST
    @Path("stop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesStop(@HeaderParam("access_token") String accessToken, ModifyNodeSchema modifyNodeSchema) throws Exception;

    @POST
    @Path("skip")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesSkip(@HeaderParam("access_token") String accessToken, ModifyNodeSchema modifyNodeSchema) throws Exception;

    @POST
    @Path("activate")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainNodesActivate(@HeaderParam("access_token") String accessToken, ModifyNodeSchema modifyNodeSchema) throws Exception;

}
