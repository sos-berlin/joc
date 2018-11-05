package com.sos.joc.jobchain.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainFilter;
import com.sos.joc.model.jobChain.JobChainP200;

public class JobChainResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainPTest() throws Exception {

        JobChainFilter jobChainsFilterSchema = new JobChainFilter();
        jobChainsFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobChainsFilterSchema.setJobChain(GlobalsTest.JOB_CHAIN);
        JobChainResourcePImpl jobChainsPImpl = new JobChainResourcePImpl();
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainP(accessToken, jobChainsFilterSchema);
        JobChainP200 jobChainsPSchema = (JobChainP200) jobsResponse.getEntity();
        assertEquals("postJobChainPTest", GlobalsTest.JOB_CHAIN, jobChainsPSchema.getJobChain().getPath());
    }

}