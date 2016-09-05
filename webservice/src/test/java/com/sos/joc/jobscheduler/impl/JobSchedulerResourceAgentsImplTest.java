package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsImpl;
import com.sos.joc.model.jobscheduler.AgentFilterSchema;
import com.sos.joc.model.jobscheduler.Agent_;
import com.sos.joc.model.jobscheduler.AgentsVSchema;

public class JobSchedulerResourceAgentsImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentsTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        AgentFilterSchema agentFilterSchema = new AgentFilterSchema();
        ArrayList <Agent_>agents = new ArrayList<Agent_>();
        Agent_ jobSchedulerAgent = new Agent_();
        jobSchedulerAgent.setAgent("http://galadriel:4445");
        agents.add(jobSchedulerAgent);
        agentFilterSchema.setAgents(agents);
        agentFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentsImpl jobschedulerResourceAgentsImpl = new JobSchedulerResourceAgentsImpl();
        JOCDefaultResponse jobschedulerAgentsResponse = jobschedulerResourceAgentsImpl.postJobschedulerAgents(sosShiroCurrentUserAnswer.getAccessToken(), agentFilterSchema);
        AgentsVSchema agentsVSchema = (AgentsVSchema) jobschedulerAgentsResponse.getEntity();
        assertEquals("postjobschedulerAgentsTest", "http://galadriel:4445", agentsVSchema.getAgents().get(0).getUrl());
     }

}

