package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourcePImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;
import com.sos.joc.response.JobSchedulerPResponse;

public class JobSchedulerResourcePImplTest {
    private static final String LDAP_PASSWORD = "root";
    private static final String LDAP_USER = "secret";
     
    @Test
    public void postjobschedulerPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourcePImpl jobschedulerResourcePImpl = new JobSchedulerResourcePImpl();
        JobSchedulerPResponse jobschedulerResponse = jobschedulerResourcePImpl.postJobschedulerP(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        Jobscheduler200PSchema jobscheduler200PSchema = (Jobscheduler200PSchema) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerPTest", 4000, jobscheduler200PSchema.getJobscheduler().getPort().intValue());
     }

}

