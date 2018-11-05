package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainV200;

public class JobChainResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainTest() throws Exception {

        JobChainFilter jobChainsFilterSchema = new JobChainFilter();
        jobChainsFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobChainsFilterSchema.setJobChain(GlobalsTest.JOB_CHAIN);
        JobChainResourceImpl jobChainsImpl = new JobChainResourceImpl();
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChain(accessToken, jobChainsFilterSchema);
        JobChainV200 jobChain200VSchema = (JobChainV200) jobsResponse.getEntity();
        assertEquals("postJobChainTest", GlobalsTest.JOB_CHAIN, jobChain200VSchema.getJobChain().getPath());
    }

}