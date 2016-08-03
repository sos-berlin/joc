package com.sos.jocjobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceSupervisorImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.Jobscheduler200VSchema;
import com.sos.joc.response.JobschedulerResponse;

public class JobSchedulerResourceSupervisorImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerSupervisorTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceSupervisorImpl jobschedulerResourceSupervisorImpl = new JobSchedulerResourceSupervisorImpl();
        JobschedulerResponse jobschedulerResponse = jobschedulerResourceSupervisorImpl.postJobschedulerSupervisor(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        Jobscheduler200VSchema jobschedulerSupervisorSchema = (Jobscheduler200VSchema) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerJobSchedulerResourceClusterImplTest.javaTest", "supervisor_scheduler_id", jobschedulerSupervisorSchema.getJobscheduler().getJobschedulerId());
     }

}

