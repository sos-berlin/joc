package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsPImpl;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentsP;

public class JobSchedulerResourceAgentsPImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerAgentsPTest() throws Exception {

        AgentFilter agentFilterSchema = new AgentFilter();
        ArrayList<AgentUrl> agents = new ArrayList<AgentUrl>();
        AgentUrl jobSchedulerAgent = new AgentUrl();
        jobSchedulerAgent.setAgent("http://galadriel:40412");
        agents.add(jobSchedulerAgent);
        agentFilterSchema.setAgents(agents);
        agentFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceAgentsPImpl jobschedulerResourceAgentsPImpl = new JobSchedulerResourceAgentsPImpl();
        JOCDefaultResponse jobschedulerAgentsPResponse = jobschedulerResourceAgentsPImpl.postJobschedulerAgentsP(accessToken, agentFilterSchema);
        AgentsP agentsPSchema = (AgentsP) jobschedulerAgentsPResponse.getEntity();
        assertEquals("postjobschedulerAgentsPTest", "http://galadriel:40412", agentsPSchema.getAgents().get(0).getUrl());
    }

}
