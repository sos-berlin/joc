package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChain200VSchema;
import com.sos.joc.model.jobChain.JobChainFilterSchema;

public class JobChainResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainFilterSchema jobChainsFilterSchema = new JobChainFilterSchema();
        jobChainsFilterSchema.setJobschedulerId("scheduler_current");
        JobChainResourceImpl jobChainsImpl = new JobChainResourceImpl();
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChain(sosShiroCurrentUserAnswer.getAccessToken(), jobChainsFilterSchema);
        JobChain200VSchema jobChain200VSchema = (JobChain200VSchema) jobsResponse.getEntity();
        assertEquals("postJobChainTest", "myPath", jobChain200VSchema.getJobChain().getPath());
    }

}