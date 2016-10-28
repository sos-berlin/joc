package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainV200;

public class JobChainResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainFilter jobChainsFilterSchema = new JobChainFilter();
        jobChainsFilterSchema.setJobschedulerId("scheduler_current");
        JobChainResourceImpl jobChainsImpl = new JobChainResourceImpl();
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChain(sosShiroCurrentUserAnswer.getAccessToken(), jobChainsFilterSchema);
        JobChainV200 jobChain200VSchema = (JobChainV200) jobsResponse.getEntity();
        assertEquals("postJobChainTest", "myPath", jobChain200VSchema.getJobChain().getPath());
    }

}