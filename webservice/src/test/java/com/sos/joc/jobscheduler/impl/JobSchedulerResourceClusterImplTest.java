package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerDefaultBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceCluster.JobschedulerClusterResponse;
import com.sos.joc.model.jobscheduler.ClusterSchema;

public class JobSchedulerResourceClusterImplTest {
    private static final String LDAP_PASSWORD = "root";
    private static final String LDAP_USER = "secret";
     
    @Test
    public void postjobschedulerClusterTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerDefaultBody jobSchedulerDefaultBody = new JobSchedulerDefaultBody();
        jobSchedulerDefaultBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceClusterImpl jobschedulerResourceClusterImpl = new JobSchedulerResourceClusterImpl();
        JobschedulerClusterResponse jobschedulerClusterResponse = jobschedulerResourceClusterImpl.postJobschedulerCluster(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerDefaultBody);
        ClusterSchema jobscheduler200VSchema = (ClusterSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", "scheduler_current", jobscheduler200VSchema.getCluster().getJobschedulerId());
     }

}

