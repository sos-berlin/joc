package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesP;

public class SchedulesResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postSchedulesPTest() throws Exception {
        SchedulesFilter schedulesFilterSchema = new SchedulesFilter();
        schedulesFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        SchedulesResourcePImpl schedulesResourcePImpl = new SchedulesResourcePImpl();
        JOCDefaultResponse jobsResponse = schedulesResourcePImpl.postSchedulesP(accessToken, schedulesFilterSchema);
        SchedulesP schedulesPSchema = (SchedulesP) jobsResponse.getEntity();
        assertEquals("postSchedulesPTest", GlobalsTest.JOB_USED, schedulesPSchema.getSchedules().get(0).getUsedByJobs().get(0).getJob());
    }

}
