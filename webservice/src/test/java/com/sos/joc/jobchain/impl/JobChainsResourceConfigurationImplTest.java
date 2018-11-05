package com.sos.joc.jobchain.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.jobChain.JobChainConfigurationFilter;

public class JobChainsResourceConfigurationImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postJobChainConfigurationTest() throws Exception {

        JobChainConfigurationFilter jobChainFilterSchema = new JobChainConfigurationFilter();
        jobChainFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobChainFilterSchema.setJobChain(GlobalsTest.JOB_CHAIN);
        JobChainResourceConfigurationImpl jobChainsImpl = new JobChainResourceConfigurationImpl();
        JOCDefaultResponse response = jobChainsImpl.postJobChainConfiguration(accessToken, jobChainFilterSchema);
        Configuration200 configurationSchema = (Configuration200) response.getEntity();
        assertEquals("postJobChainConfigurationTest", GlobalsTest.JOB_CHAIN, configurationSchema.getConfiguration().getPath());
    }

}