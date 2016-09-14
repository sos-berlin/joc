package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChain200PSchema;
import com.sos.joc.model.jobChain.JobChainFilterSchema;

public class JobChainResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainFilterSchema jobChainsFilterSchema = new JobChainFilterSchema();
        jobChainsFilterSchema.setJobschedulerId("scheduler_current");
        JobChainResourcePImpl jobChainsPImpl = new JobChainResourcePImpl();
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainP(sosShiroCurrentUserAnswer.getAccessToken(), jobChainsFilterSchema);
        JobChain200PSchema jobChainsPSchema = (JobChain200PSchema) jobsResponse.getEntity();
        assertEquals("postJobChainPTest", "myPath", jobChainsPSchema.getJobChain().getPath());
    }

}