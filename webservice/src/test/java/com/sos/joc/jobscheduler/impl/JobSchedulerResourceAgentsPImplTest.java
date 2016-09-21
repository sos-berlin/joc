package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsPImpl;
import com.sos.joc.model.jobscheduler.AgentFilterSchema;
import com.sos.joc.model.jobscheduler.Agent_;
import com.sos.joc.model.jobscheduler.AgentsPSchema;

public class JobSchedulerResourceAgentsPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentsPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        AgentFilterSchema agentFilterSchema = new AgentFilterSchema();
        ArrayList <Agent_>agents = new ArrayList<Agent_>();
        Agent_ jobSchedulerAgent = new Agent_();
        jobSchedulerAgent.setAgent("http://galadriel:4445");
        agents.add(jobSchedulerAgent);
        agentFilterSchema.setAgents(agents);
        agentFilterSchema.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentsPImpl jobschedulerResourceAgentsPImpl = new JobSchedulerResourceAgentsPImpl();
        JOCDefaultResponse jobschedulerAgentsPResponse = jobschedulerResourceAgentsPImpl.postJobschedulerAgentsP(sosShiroCurrentUserAnswer.getAccessToken(), agentFilterSchema);
        AgentsPSchema agentsPSchema = (AgentsPSchema) jobschedulerAgentsPResponse.getEntity();
        assertEquals("postjobschedulerAgentsPTest", "http://galadriel:4445", agentsPSchema.getAgents().get(0).getUrl());
     }

}

