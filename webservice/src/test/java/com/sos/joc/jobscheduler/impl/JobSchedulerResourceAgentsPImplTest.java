package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsPImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgent;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentsBody;
import com.sos.joc.model.jobscheduler.AgentsPSchema;

public class JobSchedulerResourceAgentsPImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentsPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerAgentsBody jobSchedulerAgentsBody = new JobSchedulerAgentsBody();
        ArrayList <JobSchedulerAgent>agents = new ArrayList<JobSchedulerAgent>();
        JobSchedulerAgent jobSchedulerAgent = new JobSchedulerAgent();
        jobSchedulerAgent.setAgent("http://galadriel:4445");
        agents.add(jobSchedulerAgent);
        jobSchedulerAgentsBody.setAgents(agents);
        jobSchedulerAgentsBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentsPImpl jobschedulerResourceAgentsPImpl = new JobSchedulerResourceAgentsPImpl();
        JOCDefaultResponse jobschedulerAgentsPResponse = jobschedulerResourceAgentsPImpl.postJobschedulerAgentsP(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerAgentsBody);
        AgentsPSchema agentsPSchema = (AgentsPSchema) jobschedulerAgentsPResponse.getEntity();
        assertEquals("postjobschedulerAgentsPTest", "http://galadriel:4445", agentsPSchema.getAgents().get(0).getUrl());
     }

}

