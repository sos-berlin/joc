package com.sos.joc.schedule.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.schedule.ScheduleV200;
import com.sos.joc.model.schedule.ScheduleFilter;



public class ScheduleResourceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceImplTest.class);
    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postScheduleTest() throws Exception {
        ScheduleFilter scheduleFilterSchema = new ScheduleFilter();
        scheduleFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        scheduleFilterSchema.setSchedule(GlobalsTest.SCHEDULE);
        ScheduleResourceImpl scheduleResourceImpl = new ScheduleResourceImpl();
        JOCDefaultResponse jobsResponse = scheduleResourceImpl.postSchedule(accessToken, scheduleFilterSchema);
        ScheduleV200 scheduleVSchema = (ScheduleV200) jobsResponse.getEntity();
        assertEquals("postScheduleTest", GlobalsTest.SCHEDULE, scheduleVSchema.getSchedule().getPath());
        LOGGER.info(jobsResponse.toString());
    }

}
