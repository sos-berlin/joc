package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentClustersPImpl;
import com.sos.joc.model.jobscheduler.AgentClusterFilterSchema;
import com.sos.joc.model.jobscheduler.AgentClustersPSchema;

public class JobSchedulerResourceAgentClustersPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentClustersPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        AgentClusterFilterSchema agentClusterFilterSchema = new AgentClusterFilterSchema();
        agentClusterFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentClustersPImpl jobschedulerResourceAgentClustersPImpl = new JobSchedulerResourceAgentClustersPImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceAgentClustersPImpl.postJobschedulerAgentClustersP(sosShiroCurrentUserAnswer.getAccessToken(), agentClusterFilterSchema);
        AgentClustersPSchema agentClustersPSchema = (AgentClustersPSchema) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerAgentClustersPTest", -1, agentClustersPSchema.getAgentClusters().get(0).getNumOfAgents().getAny().intValue());
     }

}

