package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesP;

public class SchedulesResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postSchedulesPTest() throws Exception {
        SchedulesFilter schedulesFilterSchema = new SchedulesFilter();
        schedulesFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        SchedulesResourcePImpl schedulesResourcePImpl = new SchedulesResourcePImpl();
        JOCDefaultResponse jobsResponse = schedulesResourcePImpl.postSchedulesP(accessToken, schedulesFilterSchema);
        SchedulesP schedulesPSchema = (SchedulesP) jobsResponse.getEntity();
        assertEquals("postSchedulesPTest", TestEnvWebserviceTest.JOB_USED, schedulesPSchema.getSchedules().get(0).getUsedByJobs().get(0).getJob());
    }

}
