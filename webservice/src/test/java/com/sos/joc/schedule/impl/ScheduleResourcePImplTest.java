package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.model.schedule.ScheduleFilter;

public class ScheduleResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postschedulePTest() throws Exception {
        ScheduleFilter scheduleFilterSchema = new ScheduleFilter();
        scheduleFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        scheduleFilterSchema.setSchedule(TestEnvWebserviceTest.SCHEDULE);
        ScheduleResourcePImpl scheduleResourcePImpl = new ScheduleResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(scheduleFilterSchema);
        JOCDefaultResponse jobsResponse = scheduleResourcePImpl.postScheduleP(accessToken, b);
        ScheduleP200 schedulePSchema = (ScheduleP200) jobsResponse.getEntity();
        assertEquals("postschedulePTest", TestEnvWebserviceTest.JOB_USED, schedulePSchema.getSchedule().getUsedByJobs().get(0).getJob());
    }

}
