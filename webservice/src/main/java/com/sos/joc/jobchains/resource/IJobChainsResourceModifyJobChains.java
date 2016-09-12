
package com.sos.joc.jobchains.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.ModifySchema;

public interface IJobChainsResourceModifyJobChains {

    @POST
    @Path("stop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainsStop(@HeaderParam("access_token") String accessToken, ModifySchema modifySchema) throws Exception;

    @POST
    @Path("unstop")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChainsUnStop(@HeaderParam("access_token") String accessToken, ModifySchema modifySchema) throws Exception;

}
