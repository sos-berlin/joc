package com.sos.jocjobscheduler.impl;

import static org.junit.Assert.*;
 

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobschedulerResourceTerminateImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerTerminateBody;

public class JobSchedulerResourceTerminateImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerTerminateTest() throws Exception   {
           
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerTerminateBody jobSchedulerTerminateBody = new JobSchedulerTerminateBody();
        jobSchedulerTerminateBody.setJobschedulerId("scheduler_current");
        jobSchedulerTerminateBody.setTimeout(30);
        JobschedulerResourceTerminateImpl jobschedulerResourceTerminateImpl = new JobschedulerResourceTerminateImpl();
        jobschedulerResourceTerminateImpl.postjobschedulerTerminate(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerTerminateBody);
    }

}
