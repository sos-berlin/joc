package com.sos.joc.job.impl;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.job.JobConfigurationFilterSchema;
import com.sos.joc.model.job.JobConfigurationFilterSchema.Mime;
 
public class JobResourceConfigurationImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceConfigurationImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";
     
    @Test
    public void postJobConfigurationDefaultTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobConfigurationFilterSchema jobConfigurationFilterSchema = new JobConfigurationFilterSchema();
        jobConfigurationFilterSchema.setJob("check_history/check");
        jobConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobConfigurationFilterSchema);
        ConfigurationSchema configurationSchema = (ConfigurationSchema) jobsResponse.getEntity();
        assertEquals("postJobConfigurationTest","check_history/check", configurationSchema.getConfiguration().getPath());
     }

    @Test
    public void postJobConfigurationHtmlTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobConfigurationFilterSchema jobConfigurationFilterSchema = new JobConfigurationFilterSchema();
        jobConfigurationFilterSchema.setJob("check_history/check");
        jobConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        jobConfigurationFilterSchema.setMime(Mime.HTML);
        JobResourceConfigurationImpl jobConfigurationImpl = new JobResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = jobConfigurationImpl.postJobConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), jobConfigurationFilterSchema);
        ConfigurationSchema configurationSchema = (ConfigurationSchema) jobsResponse.getEntity();
        assertNotNull("postJobConfigurationTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
     }

}

