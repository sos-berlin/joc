package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsPImpl;
import com.sos.joc.model.jobscheduler.AgentFilter;
import com.sos.joc.model.jobscheduler.AgentUrl;
import com.sos.joc.model.jobscheduler.AgentsP;

public class JobSchedulerResourceAgentsPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentsPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        AgentFilter agentFilterSchema = new AgentFilter();
        ArrayList <AgentUrl>agents = new ArrayList<AgentUrl>();
        AgentUrl jobSchedulerAgent = new AgentUrl();
        jobSchedulerAgent.setAgent("http://galadriel:4445");
        agents.add(jobSchedulerAgent);
        agentFilterSchema.setAgents(agents);
        agentFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentsPImpl jobschedulerResourceAgentsPImpl = new JobSchedulerResourceAgentsPImpl();
        JOCDefaultResponse jobschedulerAgentsPResponse = jobschedulerResourceAgentsPImpl.postJobschedulerAgentsP(sosShiroCurrentUserAnswer.getAccessToken(), agentFilterSchema);
        AgentsP agentsPSchema = (AgentsP) jobschedulerAgentsPResponse.getEntity();
        assertEquals("postjobschedulerAgentsPTest", "http://galadriel:4445", agentsPSchema.getAgents().get(0).getUrl());
     }

}

