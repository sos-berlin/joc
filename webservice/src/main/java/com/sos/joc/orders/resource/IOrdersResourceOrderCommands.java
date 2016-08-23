package com.sos.joc.orders.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sos.joc.orders.post.commands.start.OrdersStartBody;
import com.sos.joc.response.JOCCockpitResponse;

public interface IOrdersResourceOrderCommands {
    
    @POST
    @Path("start")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersStart(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

    
    @POST
    @Path("add")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersAdd(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

    
    
    @POST
    @Path("suspend")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersSuspend(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

    @POST
    @Path("resume")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersResume(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("reset")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersReset(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("delete")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersDelete(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

    @POST
    @Path("set_state")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersStartSetState(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;

           
    @POST
    @Path("set_run_time")
    @Consumes("application/json")
    @Produces({ "application/json" })
    public JOCCockpitResponse postOrdersStartSetRunTime(
            @HeaderParam("access_token") String accessToken, OrdersStartBody jobSchedulerTerminateBody) throws Exception;
        
}
