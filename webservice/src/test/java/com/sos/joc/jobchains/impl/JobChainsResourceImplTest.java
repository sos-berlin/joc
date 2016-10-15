package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsV;

public class JobChainsResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postJobChainsTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainsFilter jobChainsFilterSchema = new JobChainsFilter();
        jobChainsFilterSchema.setJobschedulerId("scheduler_current");
        JobChainsResourceImpl jobChainsImpl = new JobChainsResourceImpl();
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChains(sosShiroCurrentUserAnswer.getAccessToken(), jobChainsFilterSchema);
        JobChainsV jobChainsVSchema = (JobChainsV) jobsResponse.getEntity();
        assertEquals("postJobChainsTest", "myPath", jobChainsVSchema.getJobChains().get(0).getPath());
    }

}