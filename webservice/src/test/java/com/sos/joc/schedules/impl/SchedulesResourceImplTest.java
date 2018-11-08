package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesV;

public class SchedulesResourceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResourceImplTest.class);
    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postSchedulesTest() throws Exception {
        SchedulesFilter schedulesFilterSchema = new SchedulesFilter();
        schedulesFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        SchedulesResourceImpl schedulesResourceImpl = new SchedulesResourceImpl();
        JOCDefaultResponse jobsResponse = schedulesResourceImpl.postSchedules(accessToken, schedulesFilterSchema);
        SchedulesV schedulesVSchema = (SchedulesV) jobsResponse.getEntity();
        assertEquals("postSchedulesTest", TestEnvWebserviceGlobalsTest.SCHEDULE, schedulesVSchema.getSchedules().get(0).getPath());
        LOGGER.info(jobsResponse.toString());
    }

}
