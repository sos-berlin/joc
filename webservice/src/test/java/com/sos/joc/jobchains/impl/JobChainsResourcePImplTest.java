package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsP;

public class JobChainsResourcePImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainsPTest() throws Exception {

        JobChainsFilter jobChainsFilterSchema = new JobChainsFilter();
        jobChainsFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        List<JobChainPath> jobChains = new  ArrayList<JobChainPath>();
        JobChainPath jobChainPath = new JobChainPath();
        jobChains.add(jobChainPath);
        jobChainPath.setJobChain(GlobalsTest.JOB_CHAIN);
        jobChainsFilterSchema.setJobChains(jobChains);
        JobChainsResourcePImpl jobChainsPImpl = new JobChainsResourcePImpl();
        JOCDefaultResponse jobsResponse = jobChainsPImpl.postJobChainsP(accessToken, jobChainsFilterSchema);
        JobChainsP jobChainsPSchema = (JobChainsP) jobsResponse.getEntity();
        assertEquals("postJobChainsPTest", GlobalsTest.JOB_CHAIN, jobChainsPSchema.getJobChains().get(0).getPath());
    }

}