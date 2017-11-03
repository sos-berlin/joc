
package com.sos.joc.jobs.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobsFilter;

 
public interface IJobsResourceOverviewSummary {

    @POST
    @Path("overview/summary")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobsOverviewSummary(            
            @HeaderParam("X-Access-Token") String xAccessToken, JobsFilter jobsFilter) throws Exception;
}
