package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.model.schedule.ScheduleFilter;
 

public class ScheduleResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
    private static final String SCHEDULER_ID = "scheduler_4444";

    @Test
    public void postschedulePTest() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ScheduleFilter scheduleFilterSchema = new ScheduleFilter();
        scheduleFilterSchema.setJobschedulerId(SCHEDULER_ID);
        ScheduleResourcePImpl scheduleResourcePImpl = new ScheduleResourcePImpl();
        JOCDefaultResponse jobsResponse = scheduleResourcePImpl.postScheduleP(sosShiroCurrentUserAnswer.getAccessToken(), scheduleFilterSchema);
        ScheduleP200 schedulePSchema = (ScheduleP200) jobsResponse.getEntity();
        assertEquals("postschedulePTest", "myUsedByJob", schedulePSchema.getSchedule().getUsedByJobs().get(0).getJob());
     }

}
