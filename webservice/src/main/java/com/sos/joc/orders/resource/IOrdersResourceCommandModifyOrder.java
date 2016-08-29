package com.sos.joc.orders.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.orders.post.commands.modify.ModifyOrdersBody;

public interface IOrdersResourceCommandModifyOrder {

    @POST
    @Path("start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersStart(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBody) throws Exception;

    @POST
    @Path("suspend")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersSuspend(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBody) throws Exception;

    @POST
    @Path("resume")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersResume(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBody) throws Exception;

    @POST
    @Path("reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersReset(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBody) throws Exception;

    @POST
    @Path("set_state")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersSetState(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBody) throws Exception;

    @POST
    @Path("set_run_time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrdersSetRunTime(@HeaderParam("access_token") String accessToken, ModifyOrdersBody modifyOrdersBod) throws Exception;

}
