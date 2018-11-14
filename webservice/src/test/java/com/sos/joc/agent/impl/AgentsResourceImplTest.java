package com.sos.joc.agent.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.report.Agents;
import com.sos.joc.model.report.AgentsFilter;
import com.sos.joc.report.impl.AgentsResourceImpl;

public class AgentsResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postAgentsReportTest() throws Exception {
        AgentsFilter agentsFilter = new AgentsFilter();
        agentsFilter.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        AgentsResourceImpl agentsImpl = new AgentsResourceImpl();
        List<String> agentUrls = new ArrayList<String>();
        agentUrls.add("http://galadriel:4445");
        agentsFilter.setAgents(agentUrls);
        agentsFilter.setDateFrom("-7d");
        agentsFilter.setDateTo("0d");
        agentsFilter.setTimeZone("Europe/Berlin");
        JOCDefaultResponse response = agentsImpl.postAgentsReport(accessToken, agentsFilter);
        Agents agents = (Agents) response.getEntity();
        assertEquals("postAgentsReportTest", "SP_4012", agents.getAgents().get(0).getJobschedulerId());
    }
}
