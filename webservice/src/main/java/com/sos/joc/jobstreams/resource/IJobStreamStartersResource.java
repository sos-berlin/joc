package com.sos.joc.jobstreams.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobStreamStartersResource {

   

    @POST
    @Path("get_jobstream_starter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse getJobStreamStarter(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamStartersFilter);


    @POST
    @Path("edit_jobstream_starters")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse editJobStreamStarters(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamStartersFilter);

    @POST
    @Path("delete_jobstream_starters")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse deleteJobStreamStarters(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamStartersFilter);
    

    @POST
    @Path("start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse startJobStreamStarters(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamStartersFilter);
}
