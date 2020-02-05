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
import com.sos.joc.model.jobChain.JobChainsV;

public class JobChainsResourceImplTest {

    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainsTest() throws Exception {

        JobChainsFilter jobChainsFilterSchema = new JobChainsFilter();
        jobChainsFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        List<JobChainPath> jobChains = new  ArrayList<JobChainPath>();
        JobChainPath jobChainPath = new JobChainPath();
        jobChainPath.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        jobChains.add(jobChainPath);
        jobChainsFilterSchema.setJobChains(jobChains);
        JobChainsResourceImpl jobChainsImpl = new JobChainsResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobChainsFilterSchema);
        JOCDefaultResponse jobsResponse = jobChainsImpl.postJobChains(accessToken, b);
        JobChainsV jobChainsVSchema = (JobChainsV) jobsResponse.getEntity();
        assertEquals("postJobChainsTest", TestEnvWebserviceTest.JOB_CHAIN, jobChainsVSchema.getJobChains().get(0).getPath());
    }

}