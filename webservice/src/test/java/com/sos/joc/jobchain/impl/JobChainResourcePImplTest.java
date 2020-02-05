package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainP200;

public class JobChainResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainPTest() throws Exception {

        JobChainFilter jobChainsFilterSchema = new JobChainFilter();
        jobChainsFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobChainsFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        JobChainResourcePImpl jobChainsPImpl = new JobChainResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobChainsFilterSchema);
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainP(accessToken, b);
        JobChainP200 jobChainsPSchema = (JobChainP200) jobsResponse.getEntity();
        assertEquals("postJobChainPTest", TestEnvWebserviceTest.JOB_CHAIN, jobChainsPSchema.getJobChain().getPath());
    }

}