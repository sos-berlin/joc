package com.sos.joc.tasks.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
 
public interface ITasksResourceKill {

    @POST
    @Path("terminate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksTerminate(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyTasksSchema);

    @POST
    @Path("terminate_within")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksTerminateWithin(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyTasksSchema);

    @POST
    @Path("kill")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksKill(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyTasksSchema);

    @POST
    @Path("end")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postTasksEnd(@HeaderParam("X-Access-Token") String accessToken, byte[] modifyTasksSchema);

}
