package com.sos.joc.orders.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.orders.post.commands.start.OrdersModifyOrderBody;
import com.sos.joc.response.JOCDefaultResponse;

public interface IOrdersResourceOrderCommands {
    
    @POST
    @Path("start")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersStart(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

    
    @POST
    @Path("add")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersAdd(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

    
    
    @POST
    @Path("suspend")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersSuspend(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

    @POST
    @Path("resume")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersResume(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

           
    @POST
    @Path("reset")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersReset(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

           
    @POST
    @Path("delete")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersDelete(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

    @POST
    @Path("set_state")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersSetState(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;

           
    @POST
    @Path("set_run_time")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCDefaultResponse postOrdersSetRunTime(
            @HeaderParam("access_token") String accessToken, OrdersModifyOrderBody modifyOrderBody) throws Exception;
        
}
