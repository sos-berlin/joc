package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.schedule.SchedulesFilterSchema;
import com.sos.joc.model.schedule.SchedulesVSchema;

public class SchedulesResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResourceImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postSchedulesTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        SchedulesFilterSchema schedulesFilterSchema = new SchedulesFilterSchema();
        schedulesFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ScheduleResourceImpl schedulesResourceImpl = new ScheduleResourceImpl();
        JOCDefaultResponse jobsResponse = schedulesResourceImpl.postSchedules(sosShiroCurrentUserAnswer.getAccessToken(), schedulesFilterSchema);
        SchedulesVSchema schedulesVSchema = (SchedulesVSchema) jobsResponse.getEntity();
        assertEquals("postSchedulesTest", "myName", schedulesVSchema.getSchedules().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

}