
package com.sos.joc.order.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sos.joc.annotation.CompressedAlready;
import com.sos.joc.classes.JOCDefaultResponse;

public interface IOrderLogResource {

    @POST
    @Path("log")
    @CompressedAlready
    @Consumes("application/json")
    //@Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse postOrderLog(@HeaderParam("X-Access-Token") String accessToken, byte[] orderHistoryFilter);

    @GET
    @Path("log/html")
    @CompressedAlready
    //@Produces({ MediaType.TEXT_HTML })
    public JOCDefaultResponse getOrderLogHtml(@HeaderParam("X-Access-Token") String accessToken,
            @QueryParam("accessToken") String queryAccessToken, @QueryParam("jobschedulerId") String jobschedulerId,
            @QueryParam("orderId") String orderId, @QueryParam("jobChain") String jobChain, @QueryParam("historyId") String historyId,
            @QueryParam("filename") String filename);

    @GET
    @Path("log/download")
    @CompressedAlready
    //@Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse downloadOrderLog(@HeaderParam("X-Access-Token") String accessToken,
            @QueryParam("accessToken") String queryAccessToken, @QueryParam("jobschedulerId") String jobschedulerId,
            @QueryParam("orderId") String orderId, @QueryParam("jobChain") String jobChain, @QueryParam("historyId") String historyId,
            @QueryParam("filename") String filename);

    @POST
    @Path("log/download")
    @CompressedAlready
    @Consumes("application/json")
    //@Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public JOCDefaultResponse downloadOrderLog(@HeaderParam("X-Access-Token") String accessToken, byte[] orderHistoryFilter);

    @POST
    @Path("log/info")
    @Consumes("application/json")
    @Produces({ MediaType.APPLICATION_JSON })
    public JOCDefaultResponse getLogInfo(@HeaderParam("X-Access-Token") String accessToken, byte[] orderHistoryFilter);
}
