package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.model.schedule.ScheduleFilter;

public class ScheduleResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postschedulePTest() throws Exception {
        ScheduleFilter scheduleFilterSchema = new ScheduleFilter();
        scheduleFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        scheduleFilterSchema.setSchedule(TestEnvWebserviceGlobalsTest.SCHEDULE);
        ScheduleResourcePImpl scheduleResourcePImpl = new ScheduleResourcePImpl();
        JOCDefaultResponse jobsResponse = scheduleResourcePImpl.postScheduleP(accessToken, scheduleFilterSchema);
        ScheduleP200 schedulePSchema = (ScheduleP200) jobsResponse.getEntity();
        assertEquals("postschedulePTest", TestEnvWebserviceGlobalsTest.JOB_USED, schedulePSchema.getSchedule().getUsedByJobs().get(0).getJob());
    }

}
