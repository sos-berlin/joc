package com.sos.joc.jobs.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.StartJobsSchema;

public interface IJobsResourceCommandStartJob {

    @POST
    @Path("start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsStart(@HeaderParam("access_token") String accessToken, StartJobsSchema startJobsSchema) throws Exception;

}
