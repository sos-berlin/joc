package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceSupervisorImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

public class JobSchedulerResourceSupervisorImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerSupervisorTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceSupervisorImpl jobschedulerResourceSupervisorImpl = new JobSchedulerResourceSupervisorImpl();
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourceSupervisorImpl.postJobschedulerSupervisor(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        JobSchedulerV200 jobschedulerSupervisorSchema = (JobSchedulerV200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerSupervisorTest.javaTest", "supervisor_scheduler_id", jobschedulerSupervisorSchema.getJobscheduler().getJobschedulerId());
     }

}

