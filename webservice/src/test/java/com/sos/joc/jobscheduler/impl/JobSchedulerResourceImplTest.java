package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.HostPortParameter;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

public class JobSchedulerResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerTest() throws Exception {

        HostPortParameter jobSchedulerFilterSchema = new HostPortParameter();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceImpl jobschedulerResourceImpl = new JobSchedulerResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobSchedulerFilterSchema);
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourceImpl.postJobscheduler(accessToken, b);
        JobSchedulerV200 jobscheduler200VSchema = (JobSchedulerV200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerTest", 40412, jobscheduler200VSchema.getJobscheduler().getPort().intValue());
    }

}
