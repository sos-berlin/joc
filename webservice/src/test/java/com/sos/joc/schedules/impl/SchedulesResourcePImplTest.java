package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.schedule.SchedulesFilterSchema;
import com.sos.joc.model.schedule.SchedulesPSchema;

public class SchedulesResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postSchedulesPTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        SchedulesFilterSchema schedulesFilterSchema = new SchedulesFilterSchema();
        schedulesFilterSchema.setJobschedulerId(SCHEDULER_ID);
        SchedulesResourcePImpl schedulesResourcePImpl = new SchedulesResourcePImpl();
        JOCDefaultResponse jobsResponse = schedulesResourcePImpl.postSchedulesP(sosShiroCurrentUserAnswer.getAccessToken(), schedulesFilterSchema);
        SchedulesPSchema schedulesPSchema = (SchedulesPSchema) jobsResponse.getEntity();
        assertEquals("postSchedulesPTest", "myUsedByJob", schedulesPSchema.getSchedules().get(0).getUsedByJobs().get(0).getJob());
     }

}
