package com.sos.joc.joc.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IAboutResource {

    @Path("about")
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    public JOCDefaultResponse getAbout(@HeaderParam("Accept") String accept);
    
    @Path("about")
    @POST
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postAbout(@HeaderParam("Accept") String accept);
    
    @Path("version")
    @GET
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    public JOCDefaultResponse getVersion(@HeaderParam("Accept") String accept);
    
    @Path("version")
    @POST
    @Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postVersion(@HeaderParam("Accept") String accept);

}
