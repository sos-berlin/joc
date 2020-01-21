package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainV200;

public class JobChainResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainTest() throws Exception {

        JobChainFilter jobChainsFilterSchema = new JobChainFilter();
        jobChainsFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobChainsFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        JobChainResourceImpl jobChainsImpl = new JobChainResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobChainsFilterSchema);
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChain(accessToken, b);
        JobChainV200 jobChain200VSchema = (JobChainV200) jobsResponse.getEntity();
        assertEquals("postJobChainTest", TestEnvWebserviceTest.JOB_CHAIN, jobChain200VSchema.getJobChain().getPath());
    }

}