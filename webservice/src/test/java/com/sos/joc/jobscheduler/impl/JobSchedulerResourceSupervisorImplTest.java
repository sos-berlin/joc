package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceSupervisorImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;

public class JobSchedulerResourceSupervisorImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerSupervisorTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceSupervisorImpl jobschedulerResourceSupervisorImpl = new JobSchedulerResourceSupervisorImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobSchedulerFilterSchema);
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourceSupervisorImpl.postJobschedulerSupervisor(accessToken, b);
        JobSchedulerV200 jobschedulerSupervisorSchema = (JobSchedulerV200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerSupervisorTest.javaTest", "scheduler_joc_cockpit", jobschedulerSupervisorSchema.getJobscheduler()
                .getJobschedulerId());
    }

}
