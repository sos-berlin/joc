
package com.sos.joc.order.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderHistoryFilter;

public interface IOrderLogResource {

    @POST
    @Path("log")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrderLog(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception;
    
    @GET
    @Path("log/html")
    @Produces({ MediaType.TEXT_HTML })
    public JOCDefaultResponse getOrderLogHtml(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, @QueryParam("accessToken") String queryAccessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("orderId") String orderId, @QueryParam("jobChain") String jobChain,
            @QueryParam("historyId") String historyId) throws Exception;
    
    @GET
    @Path("log/download")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse downloadOrderLog(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, @QueryParam("accessToken") String queryAccessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("orderId") String orderId, @QueryParam("jobChain") String jobChain,
            @QueryParam("historyId") String historyId) throws Exception;
    
    @POST
    @Path("log/download")
    @Consumes("application/json")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse downloadOrderLog(@HeaderParam("X-Access-Token") String xAccessToken,@HeaderParam("access_token") String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception;

}

 


