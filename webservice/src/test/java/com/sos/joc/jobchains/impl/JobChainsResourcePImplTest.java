package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainsFilterSchema;
import com.sos.joc.model.jobChain.JobChainsPSchema;

public class JobChainsResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainsPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainsFilterSchema jobChainsFilterSchema = new JobChainsFilterSchema();
        jobChainsFilterSchema.setJobschedulerId("scheduler_current");
        JobChainsResourcePImpl jobChainsPImpl = new JobChainsResourcePImpl();
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainsP(sosShiroCurrentUserAnswer.getAccessToken(), jobChainsFilterSchema);
        JobChainsPSchema jobChainsPSchema = (JobChainsPSchema) jobsResponse.getEntity();
        assertEquals("postJobChainsPTest", "myPath", jobChainsPSchema.getJobChains().get(0).getPath());
    }

}