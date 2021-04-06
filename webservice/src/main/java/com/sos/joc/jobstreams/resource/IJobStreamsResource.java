package com.sos.joc.jobstreams.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IJobStreamsResource {

    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse getJobStreams(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamsFilter);

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse deleteJobStream(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamsFilter);

    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse editJobStream(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamsFilter);

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse addJobStream(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamsFilter);
    
    @POST
    @Path("import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    public JOCDefaultResponse importJobStreams(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamsSelector);

    @POST
    @Path("export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse exportJobStreams(@HeaderParam("X-Access-Token") String accessToken, byte[] jobstreamsSelector);
}
