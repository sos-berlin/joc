package com.sos.joc.jobchain.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;

public class JobChainsResourceConfigurationImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainConfigurationTest() throws Exception {

        JobChainConfigurationFilter jobChainFilterSchema = new JobChainConfigurationFilter();
        jobChainFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        jobChainFilterSchema.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        JobChainResourceConfigurationImpl jobChainsImpl = new JobChainResourceConfigurationImpl();
        JOCDefaultResponse response = jobChainsImpl.postJobChainConfiguration(accessToken, jobChainFilterSchema);
        Configuration200 configurationSchema = (Configuration200) response.getEntity();
        assertEquals("postJobChainConfigurationTest", TestEnvWebserviceGlobalsTest.JOB_CHAIN, configurationSchema.getConfiguration().getPath());
    }

}