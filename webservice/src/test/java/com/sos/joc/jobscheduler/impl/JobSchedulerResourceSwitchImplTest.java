package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.common.Ok;

public class JobSchedulerResourceSwitchImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postjobschedulerSwitchTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        JobSchedulerResourceSwitchImpl jobSchedulerResourceSwitchImpl = new JobSchedulerResourceSwitchImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobSchedulerResourceSwitchImpl.postJobschedulerSwitch(accessToken, jobSchedulerFilterSchema);
        Ok okSchema = (Ok) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerSwitchTest", true, okSchema.getOk());
    }

}
