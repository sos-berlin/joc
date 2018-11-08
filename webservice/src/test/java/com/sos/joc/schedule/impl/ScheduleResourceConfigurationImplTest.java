package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.schedule.Configuration200;
import com.sos.joc.model.schedule.ScheduleConfigurationFilter;

public class ScheduleResourceConfigurationImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceConfigurationImplTest.class);

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postScheduleConfigurationDefaultTest() throws Exception {
        ScheduleConfigurationFilter scheduleConfigurationFilterSchema = new ScheduleConfigurationFilter();
        scheduleConfigurationFilterSchema.setSchedule(TestEnvWebserviceGlobalsTest.SCHEDULE);
        scheduleConfigurationFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        ScheduleResourceConfigurationImpl scheduleResourceConfigurationImpl = new ScheduleResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = scheduleResourceConfigurationImpl.postScheduleConfiguration(accessToken, scheduleConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postScheduleConfigurationDefaultTest", TestEnvWebserviceGlobalsTest.SCHEDULE, configurationSchema.getConfiguration().getPath());
    }

    @Test
    public void postScheduleConfigurationHtmlTest() throws Exception {
        ScheduleConfigurationFilter scheduleConfigurationFilterSchema = new ScheduleConfigurationFilter();
        scheduleConfigurationFilterSchema.setSchedule(TestEnvWebserviceGlobalsTest.SCHEDULE);
        scheduleConfigurationFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        scheduleConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        ScheduleResourceConfigurationImpl scheduleResourceConfigurationImpl = new ScheduleResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = scheduleResourceConfigurationImpl.postScheduleConfiguration(accessToken, scheduleConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postScheduleConfigurationHtmlTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
    }

}
