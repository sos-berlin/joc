package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentClustersPImpl;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusters;

public class JobSchedulerResourceAgentClustersPImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerAgentClustersPTest() throws Exception {

        AgentClusterFilter agentClusterFilterSchema = new AgentClusterFilter();
        agentClusterFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceAgentClustersPImpl jobschedulerResourceAgentClustersPImpl = new JobSchedulerResourceAgentClustersPImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(agentClusterFilterSchema);
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceAgentClustersPImpl.postJobschedulerAgentClustersP(accessToken, b);
        AgentClusters agentClustersPSchema = (AgentClusters) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerAgentClustersPTest", 1, agentClustersPSchema.getAgentClusters().get(0).getNumOfAgents().getAny().intValue());
    }

}
