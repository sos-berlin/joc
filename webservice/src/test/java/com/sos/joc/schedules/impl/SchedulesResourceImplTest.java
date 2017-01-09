package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesV;

public class SchedulesResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResourceImplTest.class);
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postSchedulesTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        SchedulesFilter schedulesFilterSchema = new SchedulesFilter();
        schedulesFilterSchema.setJobschedulerId(SCHEDULER_ID);
        SchedulesResourceImpl schedulesResourceImpl = new SchedulesResourceImpl();
        JOCDefaultResponse jobsResponse = schedulesResourceImpl.postSchedules(sosShiroCurrentUserAnswer.getAccessToken(), schedulesFilterSchema);
        SchedulesV schedulesVSchema = (SchedulesV) jobsResponse.getEntity();
        assertEquals("postSchedulesTest", "myName", schedulesVSchema.getSchedules().get(0).getName());
        LOGGER.info(jobsResponse.toString());
    }

}
