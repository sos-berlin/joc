package com.sos.joc.jobs.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobsResourceStartJob {

    @POST
    @Path("start")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
    public JOCDefaultResponse postJobsStart(@HeaderParam("X-Access-Token") String accessToken, byte[] startJobsBytes);

}
