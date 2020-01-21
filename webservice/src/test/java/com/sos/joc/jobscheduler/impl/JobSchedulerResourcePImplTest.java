package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourcePImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;

public class JobSchedulerResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourcePImpl jobschedulerResourcePImpl = new JobSchedulerResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobSchedulerFilterSchema);
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourcePImpl.postJobschedulerP(sosShiroCurrentUserAnswer.getAccessToken(), b);
        JobSchedulerP200 jobscheduler200PSchema = (JobSchedulerP200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerPTest", 4000, jobscheduler200PSchema.getJobscheduler().getPort().intValue());
     }

}

