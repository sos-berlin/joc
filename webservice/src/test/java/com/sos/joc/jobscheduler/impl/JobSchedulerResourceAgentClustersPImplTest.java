package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentClustersPImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClustersP.JobschedulerAgentClustersPResponse;
import com.sos.joc.model.jobscheduler.AgentClustersPSchema;

public class JobSchedulerResourceAgentClustersPImplTest {
    private static final String LDAP_PASSWORD = "root";
    private static final String LDAP_USER = "secret";
     
    @Test
    public void postjobschedulerAgentClustersPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody = new JobSchedulerAgentClustersBody();
        jobSchedulerAgentClustersBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentClustersPImpl jobschedulerResourceAgentClustersPImpl = new JobSchedulerResourceAgentClustersPImpl();
        JobschedulerAgentClustersPResponse jobschedulerClusterResponse = jobschedulerResourceAgentClustersPImpl.postJobschedulerAgentClusters(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerAgentClustersBody);
        AgentClustersPSchema agentClustersPSchema = (AgentClustersPSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", -1, agentClustersPSchema.getAgentClusters().get(0).getNumOfAgents().getAny().intValue());
     }

}

