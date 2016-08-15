package com.sos.jocjobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterMembersPImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembersP.JobschedulerClusterMembersPResponse;
import com.sos.joc.model.jobscheduler.MastersPSchema;

public class JobSchedulerResourceClusterMembersPImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerClusterMembersPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_joc_cockpit");
        JobSchedulerResourceClusterMembersPImpl jobschedulerResourceClusterMembersPImpl = new JobSchedulerResourceClusterMembersPImpl();
        JobschedulerClusterMembersPResponse jobschedulerClusterMembersPResponse = jobschedulerResourceClusterMembersPImpl.postJobschedulerClusterMembers(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        MastersPSchema mastersPSchema = (MastersPSchema) jobschedulerClusterMembersPResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", "scheduler_joc_cockpit", mastersPSchema.getMasters().get(0).getJobschedulerId());
     }

}

