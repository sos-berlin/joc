package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.jobscheduler.JobSchedulerIds;

public class JobSchedulerResourceIdsImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerIdsTest() throws Exception {

        JobSchedulerResourceIdsImpl jobschedulerResourceIdsImpl = new JobSchedulerResourceIdsImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceIdsImpl.postJobschedulerIds(accessToken);
        JobSchedulerIds idsSchema = (JobSchedulerIds) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerIdsTest", "scheduler.1.11.1", idsSchema.getJobschedulerIds().get(0));
    }

}
