package com.sos.joc.yade.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;


public interface IYadeFileResource {

    @POST
    @Path("file")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postYadeFile(@HeaderParam("X-Access-Token") String accessToken, byte[] filterBody);
}
