package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.common.Ok;

public class JobSchedulerResourceSwitchImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerSwitchTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceSwitchImpl jobSchedulerResourceSwitchImpl = new JobSchedulerResourceSwitchImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobSchedulerFilterSchema);
        JOCDefaultResponse jobschedulerClusterResponse = jobSchedulerResourceSwitchImpl.postJobschedulerSwitch(accessToken, b);
        Ok okSchema = (Ok) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerSwitchTest", true, okSchema.getOk());
    }

}
