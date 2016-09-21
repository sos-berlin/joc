package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.jobscheduler.JobschedulerIdsSchema;

public class JobSchedulerResourceIdsImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerIdsTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerResourceIdsImpl jobschedulerResourceIdsImpl = new JobSchedulerResourceIdsImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceIdsImpl.postJobschedulerIds(sosShiroCurrentUserAnswer.getAccessToken());
        JobschedulerIdsSchema idsSchema = (JobschedulerIdsSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerIdsTest", "re-dell_4444_jobscheduler.1.9.5x86-snapshot", idsSchema.getJobschedulerIds().get(0));
     }

}

