package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentClustersPImpl;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClustersP;

public class JobSchedulerResourceAgentClustersPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentClustersPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        AgentClusterFilter agentClusterFilterSchema = new AgentClusterFilter();
        agentClusterFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentClustersPImpl jobschedulerResourceAgentClustersPImpl = new JobSchedulerResourceAgentClustersPImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceAgentClustersPImpl.postJobschedulerAgentClustersP(sosShiroCurrentUserAnswer.getAccessToken(), agentClusterFilterSchema);
        AgentClustersP agentClustersPSchema = (AgentClustersP) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerAgentClustersPTest", -1, agentClustersPSchema.getAgentClusters().get(0).getNumOfAgents().getAny().intValue());
     }

}

