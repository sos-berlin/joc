package com.sos.joc.joe.lock.resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.joe.lock.LockEdit;
 
public interface IWriteLockConfigurationResource {

    @POST
    @Path("writelock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse writeLocks(            
            @HeaderParam("X-Access-Token") String accessToken, LockEdit lock) throws Exception;
}
