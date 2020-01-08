package com.sos.joc.joe.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.joe.wizard.JobFilter;
import com.sos.joc.model.joe.wizard.JobsFilter;

public interface IWizardResource {
    
    @POST
    @Path("jobs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobs(@HeaderParam("X-Access-Token") final String accessToken, final JobsFilter body);
    
    @POST
    @Path("job")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJob(@HeaderParam("X-Access-Token") final String accessToken, final JobFilter body);

}
