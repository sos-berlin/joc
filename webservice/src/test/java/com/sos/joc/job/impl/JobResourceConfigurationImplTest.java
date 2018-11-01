package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.job.JobConfigurationFilter;
 
public class JobResourceConfigurationImplTest {
    private static final String PASSWORD = "root";
    private static final String USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_1.12";
     
    @Test
    public void postJobConfigurationDefaultTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        JobConfigurationFilter jobConfigurationFilterSchema = new JobConfigurationFilter();
        jobConfigurationFilterSchema.setJob("check_history/check");
        jobConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postJobConfigurationTest","check_history/check", configurationSchema.getConfiguration().getPath());
     }

    @Test
    public void postJobConfigurationHtmlTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        JobConfigurationFilter jobConfigurationFilterSchema = new JobConfigurationFilter();
        jobConfigurationFilterSchema.setJob("check_history/check");
        jobConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        jobConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postJobConfigurationTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
     }

}

