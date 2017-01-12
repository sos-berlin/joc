package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.commands.JobChainNodeAction;
import com.sos.joc.model.commands.JobChainNodeModify;
import com.sos.joc.model.commands.JobschedulerCommands;
import com.sos.joc.model.commands.ShowJobs;
import com.sos.joc.model.commands.ShowState;

public class JobSchedulerResourceCommandImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobschedulerCommandTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobschedulerCommands jobschedulerCommand = new JobschedulerCommands();
        jobschedulerCommand.setUrl("http://localhost:4444");
        jobschedulerCommand.setJobschedulerId("scheduler_joc_cockpit");
        ShowState command = new ShowState();
        JobChainNodeModify jobChainNodeModify = new JobChainNodeModify();
        jobChainNodeModify.setAction(JobChainNodeAction.STOP);
        jobChainNodeModify.setJobChain("/job_chain1");
         jobChainNodeModify.setState("test");
         
         ShowJobs showJobs = new ShowJobs();
          
         jobschedulerCommand.getAddOrderOrCheckFoldersOrKillTask().add(command);
        JobSchedulerResourceCommandImpl orderConfigurationImpl = new JobSchedulerResourceCommandImpl();
        JOCDefaultResponse response = orderConfigurationImpl.postJobschedulerCommands(sosShiroCurrentUserAnswer.getAccessToken(), jobschedulerCommand);
        System.out.println(response.getEntity().toString());
    }

}
