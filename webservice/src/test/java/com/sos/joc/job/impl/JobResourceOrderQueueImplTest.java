package com.sos.joc.job.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.model.job.JobFilter;

public class JobResourceOrderQueueImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceOrderQueueImplTest.class);
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }


    @Test
    public void postJobOrderQueueTest() throws Exception {

        LOGGER.info("postJobOrderQueueTest start");
        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobFilterSchema.setJob(TestEnvWebserviceTest.JOB);
        JobResourceOrderQueueImpl jobOrderQueueImpl = new JobResourceOrderQueueImpl();
        jobOrderQueueImpl.postJobOrderQueue(accessToken, jobFilterSchema);
        assertEquals("postJobOrderQueueTest", TestEnvWebserviceTest.JOB, jobFilterSchema.getJob());
    }

}