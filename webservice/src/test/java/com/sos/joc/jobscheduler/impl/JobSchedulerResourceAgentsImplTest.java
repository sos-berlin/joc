package com.sos.joc.jobscheduler.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceAgentsImpl;
import com.sos.joc.jobscheduler.post.JobSchedulerAgent;
import com.sos.joc.jobscheduler.post.JobSchedulerAgentsBody;
import com.sos.joc.model.jobscheduler.AgentsVSchema;

public class JobSchedulerResourceAgentsImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postjobschedulerAgentsTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerAgentsBody jobSchedulerAgentsBody = new JobSchedulerAgentsBody();
        ArrayList <JobSchedulerAgent>agents = new ArrayList<JobSchedulerAgent>();
        JobSchedulerAgent jobSchedulerAgent = new JobSchedulerAgent();
        jobSchedulerAgent.setAgent("http://galadriel:4445");
        agents.add(jobSchedulerAgent);
        jobSchedulerAgentsBody.setAgents(agents);
        jobSchedulerAgentsBody.setJobschedulerId("scheduler_current");
        JobSchedulerResourceAgentsImpl jobschedulerResourceAgentsImpl = new JobSchedulerResourceAgentsImpl();
        JOCDefaultResponse jobschedulerAgentsResponse = jobschedulerResourceAgentsImpl.postJobschedulerAgents(sosShiroCurrentUserAnswer.getAccessToken(), jobSchedulerAgentsBody);
        AgentsVSchema agentsVSchema = (AgentsVSchema) jobschedulerAgentsResponse.getEntity();
        assertEquals("postjobschedulerAgentsTest", "http://galadriel:4445", agentsVSchema.getAgents().get(0).getUrl());
     }

}

