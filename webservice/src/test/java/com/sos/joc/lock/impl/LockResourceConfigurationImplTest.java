package com.sos.joc.lock.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.lock.LockConfigurationFilter;

public class LockResourceConfigurationImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockResourceConfigurationImplTest.class);

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postLockConfigurationDefaultTest() throws Exception {
        LockConfigurationFilter lockConfigurationFilterSchema = new LockConfigurationFilter();
        lockConfigurationFilterSchema.setLock(TestEnvWebserviceTest.LOCK);
        lockConfigurationFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        LockResourceConfigurationImpl lockResourceConfigurationImpl = new LockResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = lockResourceConfigurationImpl.postLockConfiguration(accessToken, lockConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postLockConfigurationTest", TestEnvWebserviceTest.LOCK, configurationSchema.getConfiguration().getPath());
    }

    @Test
    public void postLockConfigurationHtmlTest() throws Exception {
        LockConfigurationFilter lockConfigurationFilterSchema = new LockConfigurationFilter();
        lockConfigurationFilterSchema.setLock(TestEnvWebserviceTest.LOCK);
        lockConfigurationFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        lockConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        LockResourceConfigurationImpl lockResourceConfigurationImpl = new LockResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = lockResourceConfigurationImpl.postLockConfiguration(accessToken, lockConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postLockConfigurationHtmlTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
    }

}
