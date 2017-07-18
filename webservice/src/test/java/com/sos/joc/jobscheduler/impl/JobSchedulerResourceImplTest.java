package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

public class JobSchedulerResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        HostPortParameter jobSchedulerFilterSchema = new HostPortParameter();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceImpl jobschedulerResourceImpl = new JobSchedulerResourceImpl();
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourceImpl.postJobscheduler(sosShiroCurrentUserAnswer.getAccessToken(), sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        JobSchedulerV200 jobscheduler200VSchema = (JobSchedulerV200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerTest", 4000, jobscheduler200VSchema.getJobscheduler().getPort().intValue());
     }

}

