package com.sos.joc.jobchain.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;

public class JobChainsResourceConfigurationImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobChainConfigurationTest() throws Exception {

        JobChainConfigurationFilter jobChainFilterSchema = new JobChainConfigurationFilter();
        jobChainFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobChainFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        JobChainResourceConfigurationImpl jobChainsImpl = new JobChainResourceConfigurationImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobChainFilterSchema);
        JOCDefaultResponse response = jobChainsImpl.postJobChainConfiguration(accessToken, b);
        Configuration200 configurationSchema = (Configuration200) response.getEntity();
        assertEquals("postJobChainConfigurationTest", TestEnvWebserviceTest.JOB_CHAIN, configurationSchema.getConfiguration().getPath());
    }

}