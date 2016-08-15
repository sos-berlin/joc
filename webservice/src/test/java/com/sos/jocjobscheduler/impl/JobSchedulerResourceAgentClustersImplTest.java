package com.sos.jocjobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentClustersImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentClustersBody;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceAgentClusters.JobschedulerAgentClustersResponse;
import com.sos.joc.model.jobscheduler.AgentClustersVSchema;

public class JobSchedulerResourceAgentClustersImplTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";
     
    @Test
    public void postjobschedulerAgentClustersTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerAgentClustersBody jobSchedulerAgentClustersBody = new JobSchedulerAgentClustersBody();
        jobSchedulerAgentClustersBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentClustersImpl jobschedulerResourceAgentClustersImpl = new JobSchedulerResourceAgentClustersImpl();
        JobschedulerAgentClustersResponse jobschedulerClusterResponse = jobschedulerResourceAgentClustersImpl.postJobschedulerAgentClusters(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerAgentClustersBody);
        AgentClustersVSchema agentClustersVSchema = (AgentClustersVSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", -1, agentClustersVSchema.getAgentClusters().get(0).getNumOfAgents().getAny().intValue());
     }

}

