package com.sos.jocjobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourcePImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceP.JobschedulerPResponse;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;

public class JobSchedulerResourcePImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourcePImpl jobschedulerResourcePImpl = new JobSchedulerResourcePImpl();
        JobschedulerPResponse jobschedulerResponse = jobschedulerResourcePImpl.postJobschedulerP(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        Jobscheduler200PSchema jobscheduler200PSchema = (Jobscheduler200PSchema) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerPTest", 4000, jobscheduler200PSchema.getJobscheduler().getPort().intValue());
     }

}

