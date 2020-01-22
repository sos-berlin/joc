package com.sos.joc.jobchains.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;

public class JobChainsResourceModifyJobChainImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainsStopTest() throws Exception {

        ModifyJobChains modifySchema = new ModifyJobChains();
        modifySchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);

        List<ModifyJobChain> listOfJobChains = new ArrayList<ModifyJobChain>();
        ModifyJobChain jobChain1 = new ModifyJobChain();
        jobChain1.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        listOfJobChains.add(jobChain1);
        ModifyJobChain jobChain2 = new ModifyJobChain();
        jobChain2.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        listOfJobChains.add(jobChain2);

        modifySchema.setJobChains(listOfJobChains);

        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(modifySchema);
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsStop(accessToken, b);
        Ok okSchema = (Ok) jobsResponse.getEntity();
        assertEquals("postJobChainsStopTest", true, okSchema.getOk());
    }

    @Test
    public void postJobChainsUnStopTest() throws Exception {

        ModifyJobChains modifySchema = new ModifyJobChains();
        modifySchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);

        List<ModifyJobChain> listOfJobChains = new ArrayList<ModifyJobChain>();
        ModifyJobChain jobChain1 = new ModifyJobChain();
        jobChain1.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        listOfJobChains.add(jobChain1);
        ModifyJobChain jobChain2 = new ModifyJobChain();
        jobChain2.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        listOfJobChains.add(jobChain2);

        modifySchema.setJobChains(listOfJobChains);

        JobChainsResourceModifyJobChainsImpl jobChainsResourceCommandModifyJobChainsImpl = new JobChainsResourceModifyJobChainsImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(modifySchema);
        JOCDefaultResponse jobsResponse = jobChainsResourceCommandModifyJobChainsImpl.postJobChainsUnStop(accessToken, b);
        Ok okSchema = (Ok) jobsResponse.getEntity();
        assertEquals("postJobChainsUnStopTest", true, okSchema.getOk());
    }

}