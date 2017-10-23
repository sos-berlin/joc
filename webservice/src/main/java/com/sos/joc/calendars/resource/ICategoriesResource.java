
package com.sos.joc.calendars.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.JobSchedulerId;

 
public interface ICategoriesResource {

    @POST
    @Path("categories")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postCategories(@HeaderParam("X-Access-Token") String xAccessToken, JobSchedulerId jobschedulerId) throws Exception;
}
