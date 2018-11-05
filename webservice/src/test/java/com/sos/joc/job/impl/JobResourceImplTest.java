package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobs.impl.JobsResourceImplTest;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobV200;

public class JobResourceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsResourceImplTest.class);
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }


    @Test
    public void postJobTest() throws Exception {

        LOGGER.info("postJobTest start");
        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobFilterSchema.setJob(GlobalsTest.JOB);
        JobResourceImpl jobImpl = new JobResourceImpl();
        JOCDefaultResponse jobsResponse = jobImpl.postJob(accessToken, jobFilterSchema);
        JobV200 jobV200Schema = (JobV200) jobsResponse.getEntity();
        assertEquals("postJobTest", GlobalsTest.JOB, jobV200Schema.getJob().getPath());
    }

}