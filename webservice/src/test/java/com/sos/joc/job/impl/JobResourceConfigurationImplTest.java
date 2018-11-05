package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.job.JobConfigurationFilter;
 
public class JobResourceConfigurationImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImplTest.class);
    
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }
     
    @Test
    public void postJobConfigurationDefaultTest() throws Exception   {
        JobConfigurationFilter jobConfigurationFilterSchema = new JobConfigurationFilter();
        jobConfigurationFilterSchema.setJob(GlobalsTest.JOB);
        jobConfigurationFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(accessToken, jobConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postJobConfigurationTest",GlobalsTest.JOB, configurationSchema.getConfiguration().getPath());
     }

    @Test
    public void postJobConfigurationHtmlTest() throws Exception   {
        JobConfigurationFilter jobConfigurationFilterSchema = new JobConfigurationFilter();
        jobConfigurationFilterSchema.setJob(GlobalsTest.JOB);
        jobConfigurationFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(accessToken, jobConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postJobConfigurationTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
     }

}

