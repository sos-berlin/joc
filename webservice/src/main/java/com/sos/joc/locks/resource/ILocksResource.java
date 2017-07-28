
package com.sos.joc.locks.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.lock.LocksFilter;

 
public interface ILocksResource {

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postLocks(            
            @HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, LocksFilter locksFilter) throws Exception;


 
    
}
