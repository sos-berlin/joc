package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterMembersImpl;
import com.sos.joc.model.common.JobSchedulerFilterSchema;
import com.sos.joc.model.jobscheduler.MastersVSchema;

public class JobSchedulerResourceClusterMembersImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerClusterMembersTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerFilterSchema jobSchedulerFilterSchema = new JobSchedulerFilterSchema();
        jobSchedulerFilterSchema.setJobschedulerId("scheduler_joc_cockpit");
        JobSchedulerResourceClusterMembersImpl jobschedulerResourceClusterMembersImpl = new JobSchedulerResourceClusterMembersImpl();
        JOCDefaultResponse jobschedulerClusterMembersResponse = jobschedulerResourceClusterMembersImpl.postJobschedulerClusterMembers(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerFilterSchema);
        MastersVSchema mastersVSchema = (MastersVSchema) jobschedulerClusterMembersResponse.getEntity();
        assertEquals("postjobschedulerClusterMembersTest", "scheduler_joc_cockpit/ur_dell:4444", mastersVSchema.getMasters().get(0).getJobschedulerId());
     }

}

