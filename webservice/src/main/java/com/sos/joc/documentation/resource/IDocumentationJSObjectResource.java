package com.sos.joc.documentation.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.sos.joc.classes.JOCDefaultResponse;

public interface IDocumentationJSObjectResource {

    @GET
    @Path("order/documentation")
    public JOCDefaultResponse orderDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("jobChain") String jobChain, @QueryParam("orderId") String orderId) throws Exception;
    
    @GET
    @Path("job/documentation")
    public JOCDefaultResponse jobDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("job") String job) throws Exception;
    
    @GET
    @Path("job_chain/documentation")
    public JOCDefaultResponse jobChainDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("jobChain") String jobChain) throws Exception;
    
    @GET
    @Path("schedule/documentation")
    public JOCDefaultResponse scheduleDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("schedule") String schedule) throws Exception;
    
    @GET
    @Path("lock/documentation")
    public JOCDefaultResponse lockDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("lock") String lock) throws Exception;
    
    @GET
    @Path("process_class/documentation")
    public JOCDefaultResponse processClassDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("processClass") String processClass) throws Exception;
    
    @GET
    @Path("calendar/documentation")
    public JOCDefaultResponse calendarDocu(@HeaderParam("X-Access-Token") String xAccessToken, @QueryParam("accessToken") String accessToken,
            @QueryParam("jobschedulerId") String jobschedulerId, @QueryParam("calendar") String calendar) throws Exception;

}
