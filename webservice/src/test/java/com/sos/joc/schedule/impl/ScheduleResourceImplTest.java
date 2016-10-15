package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.schedule.ScheduleV200;
import com.sos.joc.model.schedule.ScheduleFilter;

public class ScheduleResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postScheduleTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ScheduleFilter scheduleFilterSchema = new ScheduleFilter();
        scheduleFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ScheduleResourceImpl scheduleResourceImpl = new ScheduleResourceImpl();
        JOCDefaultResponse jobsResponse = scheduleResourceImpl.postSchedule(sosShiroCurrentUserAnswer.getAccessToken(), scheduleFilterSchema);
        ScheduleV200 scheduleVSchema = (ScheduleV200) jobsResponse.getEntity();
        assertEquals("postScheduleTest", "myName", scheduleVSchema.getSchedule().getName());
        LOGGER.info(jobsResponse.toString());
    }

}
