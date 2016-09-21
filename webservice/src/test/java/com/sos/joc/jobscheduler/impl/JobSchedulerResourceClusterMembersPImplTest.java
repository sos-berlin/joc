package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterMembersPImpl;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.MastersPSchema;

public class JobSchedulerResourceClusterMembersPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerClusterMembersPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerFilterSchema jobSchedulerFilterSchema = new JobSchedulerFilterSchema();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_joc_cockpit");
        JobSchedulerResourceClusterMembersPImpl jobschedulerResourceClusterMembersPImpl = new JobSchedulerResourceClusterMembersPImpl();
        JOCDefaultResponse jobschedulerClusterMembersPResponse = jobschedulerResourceClusterMembersPImpl.postJobschedulerClusterMembers(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        MastersPSchema mastersPSchema = (MastersPSchema) jobschedulerClusterMembersPResponse.getEntity();
        assertEquals("postjobschedulerClusterMembersPTest", "scheduler_joc_cockpit", mastersPSchema.getMasters().get(0).getJobschedulerId());
     }

}

