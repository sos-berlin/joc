package com.sos.joc.schedules.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesV;

public class SchedulesResourceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulesResourceImplTest.class);
    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postSchedulesTest() throws Exception {
        SchedulesFilter schedulesFilterSchema = new SchedulesFilter();
        schedulesFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        SchedulesResourceImpl schedulesResourceImpl = new SchedulesResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(schedulesFilterSchema);
        JOCDefaultResponse jobsResponse = schedulesResourceImpl.postSchedules(accessToken, b);
        SchedulesV schedulesVSchema = (SchedulesV) jobsResponse.getEntity();
        assertEquals("postSchedulesTest", TestEnvWebserviceTest.SCHEDULE, schedulesVSchema.getSchedules().get(0).getPath());
        LOGGER.info(jobsResponse.toString());
    }

}
