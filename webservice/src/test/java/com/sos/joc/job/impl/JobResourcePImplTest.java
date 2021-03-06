package com.sos.joc.job.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.job.JobFilter;
import com.sos.joc.model.job.JobP200;

public class JobResourcePImplTest {

    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postJobPTest() throws Exception {

        JobFilter jobFilterSchema = new JobFilter();
        jobFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        jobFilterSchema.setJob(TestEnvWebserviceTest.JOB);
        JobResourcePImpl jobPImpl = new JobResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobFilterSchema);
        JOCDefaultResponse jobsResponse = jobPImpl.postJobP(accessToken, b);
        JobP200 jobP200Schema = (JobP200) jobsResponse.getEntity();
        assertEquals("postJobPTest", TestEnvWebserviceTest.JOB, jobP200Schema.getJob().getPath());
    }

}