package com.sos.joc.schedule.impl;
 
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.schedule.ScheduleConfigurationFilter;
 
public class ScheduleResourceConfigurationImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceConfigurationImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";
     
    @Test
    public void postScheduleConfigurationDefaultTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ScheduleConfigurationFilter scheduleConfigurationFilterSchema = new ScheduleConfigurationFilter();
        scheduleConfigurationFilterSchema.setSchedule("mySchedule");
        scheduleConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ScheduleResourceConfigurationImpl scheduleResourceConfigurationImpl = new ScheduleResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = scheduleResourceConfigurationImpl.postScheduleConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), scheduleConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertEquals("postScheduleConfigurationDefaultTest","myPath", configurationSchema.getConfiguration().getPath());
     }

    @Test
    public void postScheduleConfigurationHtmlTest() throws Exception   {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ScheduleConfigurationFilter scheduleConfigurationFilterSchema = new ScheduleConfigurationFilter();
        scheduleConfigurationFilterSchema.setSchedule("mySchedule");
        scheduleConfigurationFilterSchema.setJobschedulerId(SCHEDULER_ID);
        scheduleConfigurationFilterSchema.setMime(ConfigurationMime.HTML);
        ScheduleResourceConfigurationImpl scheduleResourceConfigurationImpl = new ScheduleResourceConfigurationImpl();
        JOCDefaultResponse jobsResponse = scheduleResourceConfigurationImpl.postScheduleConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), scheduleConfigurationFilterSchema);
        Configuration200 configurationSchema = (Configuration200) jobsResponse.getEntity();
        assertNotNull("postScheduleConfigurationHtmlTest", configurationSchema.getConfiguration().getContent().getHtml());
        LOGGER.info(configurationSchema.getConfiguration().getContent().getHtml());
     }

}

