
package com.sos.joc.job.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobFilterSchema;

public interface IJobRunTimeResource {

    @POST
    @Path("run_time")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobRunTime(@HeaderParam("access_token") String accessToken, JobFilterSchema jobFilterSchema) throws Exception;

}
