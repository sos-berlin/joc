package com.sos.jocjobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterMembersImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers.JobschedulerClusterMembersResponse;
import com.sos.joc.model.jobscheduler.MastersVSchema;

public class JobSchedulerResourceClusterMembersImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerClusterMembersTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_joc_cockpit");
        JobSchedulerResourceClusterMembersImpl jobschedulerResourceClusterMembersImpl = new JobSchedulerResourceClusterMembersImpl();
        JobschedulerClusterMembersResponse jobschedulerClusterMembersResponse = jobschedulerResourceClusterMembersImpl.postJobschedulerClusterMembers(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        MastersVSchema mastersVSchema = (MastersVSchema) jobschedulerClusterMembersResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", "scheduler_joc_cockpit/ur_dell:4444", mastersVSchema.getMasters().get(0).getJobschedulerId());
     }

}

