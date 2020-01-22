
package com.sos.joc.jobchains.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobChainsResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChains(@HeaderParam("X-Access-Token") String accessToken, byte[] jobChainsFilter);

}
