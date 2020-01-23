
package com.sos.joc.jobstreams.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
  
public interface IJobStreamResourceHistory {

    @POST
    @Path("history")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postJobStreamHistory(@HeaderParam("X-Access-Token") String accessToken, byte[] jobStreamFilter);
}
