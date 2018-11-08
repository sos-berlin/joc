package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceSupervisorPImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerP200;

public class JobSchedulerResourceSupervisorPImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    @Ignore
    public void postjobschedulerSupervisorPTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceSupervisorPImpl jobschedulerResourceSupervisorPImpl = new JobSchedulerResourceSupervisorPImpl();
        JOCDefaultResponse jobschedulerResponse = jobschedulerResourceSupervisorPImpl.postJobschedulerSupervisorP(accessToken,
                jobSchedulerFilterSchema);
        JobSchedulerP200 jobschedulerSupervisorSchema = (JobSchedulerP200) jobschedulerResponse.getEntity();
        assertEquals("postjobschedulerSupervisorPTest.javaTest", TestEnvWebserviceTest.SCHEDULER_ID, jobschedulerSupervisorSchema.getJobscheduler()
                .getJobschedulerId());
    }

}
