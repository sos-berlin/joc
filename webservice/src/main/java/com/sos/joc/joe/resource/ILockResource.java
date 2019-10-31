package com.sos.joc.joe.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.joe.lock.LockFilter;

public interface ILockResource {
    
    @POST
    @Path("lock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse lock(@HeaderParam("X-Access-Token") final String accessToken, final LockFilter body);
    
    @POST
    @Path("lock/release")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse release(@HeaderParam("X-Access-Token") final String accessToken, final LockFilter body);
    
    @POST
    @Path("lock/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse lockedBy(@HeaderParam("X-Access-Token") final String accessToken, final LockFilter body);

}
