package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsImpl;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentsV;

public class JobSchedulerResourceAgentsImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerAgentsTest() throws Exception {

        AgentFilter agentFilterSchema = new AgentFilter();
        ArrayList<AgentUrl> agents = new ArrayList<AgentUrl>();
        AgentUrl jobSchedulerAgent = new AgentUrl();
        jobSchedulerAgent.setAgent("http://galadriel:4445");
        agents.add(jobSchedulerAgent);
        agentFilterSchema.setAgents(agents);
        agentFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceAgentsImpl jobschedulerResourceAgentsImpl = new JobSchedulerResourceAgentsImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(agentFilterSchema);
        JOCDefaultResponse jobschedulerAgentsResponse = jobschedulerResourceAgentsImpl.postJobschedulerAgents(accessToken, b);
        AgentsV agentsVSchema = (AgentsV) jobschedulerAgentsResponse.getEntity();
        assertEquals("postjobschedulerAgentsTest", "http://galadriel:4445", agentsVSchema.getAgents().get(0).getUrl());
    }

}
