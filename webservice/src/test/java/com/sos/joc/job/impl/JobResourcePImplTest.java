package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP200;

public class JobResourcePImplTest {

    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postJobPTest() throws Exception {

        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        jobFilterSchema.setJob(GlobalsTest.JOB);
        JobResourcePImpl jobPImpl = new JobResourcePImpl();
        JOCDefaultResponse jobsResponse = jobPImpl.postJobP(accessToken, jobFilterSchema);
        JobP200 jobP200Schema = (JobP200) jobsResponse.getEntity();
        assertEquals("postJobPTest", GlobalsTest.JOB, jobP200Schema.getJob().getPath());
    }

}