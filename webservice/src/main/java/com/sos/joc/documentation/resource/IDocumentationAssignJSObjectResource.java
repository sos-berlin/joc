package com.sos.joc.documentation.resource;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.calendar.CalendarDocuFilter;
import com.sos.joc.model.job.JobDocuFilter;
import com.sos.joc.model.jobChain.JobChainDocuFilter;
import com.sos.joc.model.lock.LockDocuFilter;
import com.sos.joc.model.order.OrderDocuFilter;
import com.sos.joc.model.processClass.ProcessClassDocuFilter;
import com.sos.joc.model.schedule.ScheduleDocuFilter;

public interface IDocumentationAssignJSObjectResource {

    @POST
    @Path("order/documentation/assign")
    public JOCDefaultResponse assignOrderDocu(@HeaderParam("X-Access-Token") String xAccessToken, OrderDocuFilter filter) throws Exception;
    
    @POST
    @Path("order/documentation/unassign")
    public JOCDefaultResponse unassignOrderDocu(@HeaderParam("X-Access-Token") String xAccessToken, OrderDocuFilter filter) throws Exception;
    
    @POST
    @Path("job/documentation/assign")
    public JOCDefaultResponse assignJobDocu(@HeaderParam("X-Access-Token") String xAccessToken, JobDocuFilter filter) throws Exception;
    
    @POST
    @Path("job/documentation/unassign")
    public JOCDefaultResponse unassignJobDocu(@HeaderParam("X-Access-Token") String xAccessToken, JobDocuFilter filter) throws Exception;
    
    @POST
    @Path("job_chain/documentation/assign")
    public JOCDefaultResponse assignJobChainDocu(@HeaderParam("X-Access-Token") String xAccessToken, JobChainDocuFilter filter) throws Exception;
    
    @POST
    @Path("job_chain/documentation/unassign")
    public JOCDefaultResponse unassignJobChainDocu(@HeaderParam("X-Access-Token") String xAccessToken, JobChainDocuFilter filter) throws Exception;
    
    @POST
    @Path("schedule/documentation/assign")
    public JOCDefaultResponse assignScheduleDocu(@HeaderParam("X-Access-Token") String xAccessToken, ScheduleDocuFilter filter) throws Exception;
    
    @POST
    @Path("schedule/documentation/unassign")
    public JOCDefaultResponse unassignScheduleDocu(@HeaderParam("X-Access-Token") String xAccessToken, ScheduleDocuFilter filter) throws Exception;
    
    @POST
    @Path("lock/documentation/assign")
    public JOCDefaultResponse assignLockDocu(@HeaderParam("X-Access-Token") String xAccessToken, LockDocuFilter filter) throws Exception;
    
    @POST
    @Path("lock/documentation/unassign")
    public JOCDefaultResponse unassignLockDocu(@HeaderParam("X-Access-Token") String xAccessToken, LockDocuFilter filter) throws Exception;
    
    @POST
    @Path("process_class/documentation/assign")
    public JOCDefaultResponse assignProcessClassDocu(@HeaderParam("X-Access-Token") String xAccessToken, ProcessClassDocuFilter filter) throws Exception;
    
    @POST
    @Path("process_class/documentation/unassign")
    public JOCDefaultResponse unassignProcessClassDocu(@HeaderParam("X-Access-Token") String xAccessToken, ProcessClassDocuFilter filter) throws Exception;
    
    @POST
    @Path("calendar/documentation/assign")
    public JOCDefaultResponse assignCalendarClassDocu(@HeaderParam("X-Access-Token") String xAccessToken, CalendarDocuFilter filter) throws Exception;

    @POST
    @Path("calendar/documentation/unassign")
    public JOCDefaultResponse unassignCalendarClassDocu(@HeaderParam("X-Access-Token") String xAccessToken, CalendarDocuFilter filter) throws Exception;

}
