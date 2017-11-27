
package com.sos.joc.plan.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.plan.RunTimePlanFilter;

public interface IPlanFromRunTimeResource {

    @POST
    @Path("from_run_time")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postPlan(@HeaderParam("X-Access-Token") String xAccessToken, RunTimePlanFilter runTimeFilter) throws Exception;

}
