package com.sos.joc.jobscheduler.impl;

  

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.Globals;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceModifyJobSchedulerImpl;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;

public class JobSchedulerResourceTerminateImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerTerminateTest() throws Exception   {
           
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        HostPortTimeOutParameter urlTimeoutParamSchema = new HostPortTimeOutParameter();
        urlTimeoutParamSchema.setJobschedulerId("scheduler_current");
        urlTimeoutParamSchema.setTimeout(30);
        JobSchedulerResourceModifyJobSchedulerImpl jobschedulerResourceTerminateImpl = new JobSchedulerResourceModifyJobSchedulerImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(urlTimeoutParamSchema);
        jobschedulerResourceTerminateImpl.postJobschedulerTerminate(sosShiroCurrentUserAnswer.getAccessToken(), b);
    }
    
    @Test
    public void postjobschedulerRestartTest() throws Exception   {
           
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        HostPortTimeOutParameter urlTimeoutParamSchema = new HostPortTimeOutParameter();
        urlTimeoutParamSchema.setJobschedulerId("scheduler_current");
        urlTimeoutParamSchema.setTimeout(30);
        JobSchedulerResourceModifyJobSchedulerImpl jobschedulerResourceTerminateImpl = new JobSchedulerResourceModifyJobSchedulerImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(urlTimeoutParamSchema);
        jobschedulerResourceTerminateImpl.postJobschedulerRestartTerminate(sosShiroCurrentUserAnswer.getAccessToken(), b);
    }

}
