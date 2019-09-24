package com.sos.joc.joe.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.joe.common.JSObjectEdit;

public interface IReadFileResource {
    
    @POST
    @Path("read/file")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse readFile(@HeaderParam("X-Access-Token") final String accessToken, final JSObjectEdit body);   

}
