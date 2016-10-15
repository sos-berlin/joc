package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceSupervisorPImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;

public class JobSchedulerResourceSupervisorPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerSupervisorPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceSupervisorPImpl jobschedulerResourceSupervisorPImpl = new JobSchedulerResourceSupervisorPImpl();
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourceSupervisorPImpl.postJobschedulerSupervisorP(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        JobSchedulerP200 jobschedulerSupervisorSchema = (JobSchedulerP200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerSupervisorPTest.javaTest", "scheduler_joc_cockpit", jobschedulerSupervisorSchema.getJobscheduler().getJobschedulerId());
     }

}

