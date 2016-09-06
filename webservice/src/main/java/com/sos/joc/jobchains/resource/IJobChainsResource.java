
package com.sos.joc.jobchains.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;

public interface IJobChainsResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobChains(@HeaderParam("access_token") String accessToken, JobChainsFilterSchema jobChainsFilterSchema) throws Exception;

}
