package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentClustersImpl;
import com.sos.joc.model.jobscheduler.AgentClusterFilter;
import com.sos.joc.model.jobscheduler.AgentClusters;

public class JobSchedulerResourceAgentClustersImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerAgentClustersTest() throws Exception {

        AgentClusterFilter agentClusterFilterSchema = new AgentClusterFilter();
        agentClusterFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceAgentClustersImpl jobschedulerResourceAgentClustersImpl = new JobSchedulerResourceAgentClustersImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceAgentClustersImpl.postJobschedulerAgentClusters(accessToken,
                agentClusterFilterSchema);
        AgentClusters agentClustersVSchema = (AgentClusters) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", 1, agentClustersVSchema.getAgentClusters().get(0).getNumOfAgents().getAny().intValue());
    }

}
