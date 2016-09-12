package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.Schedule200PSchema;
import com.sos.joc.model.schedule.ScheduleFilterSchema;
 

public class ScheduleResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postschedulePTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ScheduleFilterSchema scheduleFilterSchema = new ScheduleFilterSchema();
        scheduleFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ScheduleResourcePImpl scheduleResourcePImpl = new ScheduleResourcePImpl();
        JOCDefaultResponse jobsResponse = scheduleResourcePImpl.postScheduleP(sosShiroCurrentUserAnswer.getAccessToken(), scheduleFilterSchema);
        Schedule200PSchema schedulePSchema = (Schedule200PSchema) jobsResponse.getEntity();
        assertEquals("postschedulePTest", "myUsedByJob", schedulePSchema.getSchedule().getUsedByJobs().get(0).getJob());
     }

}
