package com.sos.joc.jobchains.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsV;

public class JobChainsResourceImplTest {

    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainsTest() throws Exception {

        JobChainsFilter jobChainsFilterSchema = new JobChainsFilter();
        jobChainsFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        List<JobChainPath> jobChains = new  ArrayList<JobChainPath>();
        JobChainPath jobChainPath = new JobChainPath();
        jobChainPath.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        jobChains.add(jobChainPath);
        jobChainsFilterSchema.setJobChains(jobChains);
        JobChainsResourceImpl jobChainsImpl = new JobChainsResourceImpl();
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChains(accessToken, jobChainsFilterSchema);
        JobChainsV jobChainsVSchema = (JobChainsV) jobsResponse.getEntity();
        assertEquals("postJobChainsTest", TestEnvWebserviceGlobalsTest.JOB_CHAIN, jobChainsVSchema.getJobChains().get(0).getPath());
    }

}