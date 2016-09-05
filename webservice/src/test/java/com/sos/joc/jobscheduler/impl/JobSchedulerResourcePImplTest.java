package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourcePImpl;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;

public class JobSchedulerResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerFilterSchema jobSchedulerFilterSchema = new JobSchedulerFilterSchema();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourcePImpl jobschedulerResourcePImpl = new JobSchedulerResourcePImpl();
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourcePImpl.postJobschedulerP(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        Jobscheduler200PSchema jobscheduler200PSchema = (Jobscheduler200PSchema) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerPTest", 4000, jobscheduler200PSchema.getJobscheduler().getPort().intValue());
     }

}

