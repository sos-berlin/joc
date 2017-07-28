
package com.sos.joc.jobscheduler.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.JobSchedulerId;

 
public interface IJobSchedulerResourceSwitch {

    @POST
    @Path("switch")
    @Produces({ "application/json" })
    public JOCDefaultResponse postJobschedulerSwitch(            
            @HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, JobSchedulerId jobSchedulerFilterSchema) throws Exception;

 
    
}
