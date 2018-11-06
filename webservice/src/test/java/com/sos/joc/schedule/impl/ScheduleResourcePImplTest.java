package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleP200;
import com.sos.joc.model.schedule.ScheduleFilter;

public class ScheduleResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postschedulePTest() throws Exception {
        ScheduleFilter scheduleFilterSchema = new ScheduleFilter();
        scheduleFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        scheduleFilterSchema.setSchedule(GlobalsTest.SCHEDULE);
        ScheduleResourcePImpl scheduleResourcePImpl = new ScheduleResourcePImpl();
        JOCDefaultResponse jobsResponse = scheduleResourcePImpl.postScheduleP(accessToken, scheduleFilterSchema);
        ScheduleP200 schedulePSchema = (ScheduleP200) jobsResponse.getEntity();
        assertEquals("postschedulePTest", GlobalsTest.JOB_USED, schedulePSchema.getSchedule().getUsedByJobs().get(0).getJob());
    }

}
