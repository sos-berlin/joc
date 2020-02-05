package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsP;

public class JobChainsResourcePImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainsPTest() throws Exception {

        JobChainsFilter jobChainsFilterSchema = new JobChainsFilter();
        jobChainsFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        List<JobChainPath> jobChains = new  ArrayList<JobChainPath>();
        JobChainPath jobChainPath = new JobChainPath();
        jobChains.add(jobChainPath);
        jobChainPath.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        jobChainsFilterSchema.setJobChains(jobChains);
        JobChainsResourcePImpl jobChainsPImpl = new JobChainsResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobChainsFilterSchema);
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainsP(accessToken, b);
        JobChainsP jobChainsPSchema = (JobChainsP) jobsResponse.getEntity();
        assertEquals("postJobChainsPTest", TestEnvWebserviceTest.JOB_CHAIN, jobChainsPSchema.getJobChains().get(0).getPath());
    }

}