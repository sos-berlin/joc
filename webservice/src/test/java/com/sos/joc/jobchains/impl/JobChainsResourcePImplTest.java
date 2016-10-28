package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsP;

public class JobChainsResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainsPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainsFilter jobChainsFilterSchema = new JobChainsFilter();
        jobChainsFilterSchema.setJobschedulerId("scheduler_current");
        JobChainsResourcePImpl jobChainsPImpl = new JobChainsResourcePImpl();
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainsP(sosShiroCurrentUserAnswer.getAccessToken(), jobChainsFilterSchema);
        JobChainsP jobChainsPSchema = (JobChainsP) jobsResponse.getEntity();
        assertEquals("postJobChainsPTest", "myPath", jobChainsPSchema.getJobChains().get(0).getPath());
    }

}