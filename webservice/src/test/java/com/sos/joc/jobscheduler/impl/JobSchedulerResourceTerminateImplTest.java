package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;
 

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceModifyJobSchedulerImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerModifyJobSchedulerBody;

public class JobSchedulerResourceTerminateImplTest {
    private static final String LDAP_PASSWORD = "root";
    private static final String LDAP_USER = "secret";
     
    @Test
    public void postjobschedulerTerminateTest() throws Exception   {
           
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody = new JobSchedulerModifyJobSchedulerBody();
        jobSchedulerTerminateBody.setJobschedulerId("scheduler_current");
        jobSchedulerTerminateBody.setTimeout(30);
        JobSchedulerResourceModifyJobSchedulerImpl jobschedulerResourceTerminateImpl = new JobSchedulerResourceModifyJobSchedulerImpl();
        jobschedulerResourceTerminateImpl.postJobschedulerTerminate(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerTerminateBody);
    }
    
    @Test
    public void postjobschedulerRestartTest() throws Exception   {
           
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerModifyJobSchedulerBody jobSchedulerTerminateBody = new JobSchedulerModifyJobSchedulerBody();
        jobSchedulerTerminateBody.setJobschedulerId("scheduler_current");
        jobSchedulerTerminateBody.setTimeout(30);
        JobSchedulerResourceModifyJobSchedulerImpl jobschedulerResourceTerminateImpl = new JobSchedulerResourceModifyJobSchedulerImpl();
        jobschedulerResourceTerminateImpl.postJobschedulerRestartTerminate(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerTerminateBody);
    }

}
