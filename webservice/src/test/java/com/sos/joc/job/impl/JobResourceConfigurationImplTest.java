package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.job.JobConfigurationFilter;
 
public class JobResourceConfigurationImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImplTest.class);
    
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }
     
    @Test
    public void postJobConfigurationDefaultTest() throws Exception   {
        JobConfigurationFilter jobConfigurationFilterSchema = new JobConfigurationFilter();
        jobConfigurationFilterSchema.setJob(TestEnvWebserviceTest.JOB);
        jobConfigurationFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobConfigurationFilterSchema);
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(accessToken, b);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postJobConfigurationTest",TestEnvWebserviceTest.JOB, configurationSchema.getConfiguration().getPath());
     }

    @Test
    public void postJobConfigurationHtmlTest() throws Exception   {
        JobConfigurationFilter jobConfigurationFilterSchema = new JobConfigurationFilter();
        jobConfigurationFilterSchema.setJob(TestEnvWebserviceTest.JOB);
        jobConfigurationFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobConfigurationFilterSchema);
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(accessToken, b);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postJobConfigurationTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
     }

}

