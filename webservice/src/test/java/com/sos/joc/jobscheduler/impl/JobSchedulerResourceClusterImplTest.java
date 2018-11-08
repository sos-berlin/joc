package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.Clusters;

public class JobSchedulerResourceClusterImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postjobschedulerClusterTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        JobSchedulerResourceClusterImpl jobschedulerResourceClusterImpl = new JobSchedulerResourceClusterImpl();
        JOCDefaultResponse jobschedulerClusterResponse = jobschedulerResourceClusterImpl.postJobschedulerCluster(accessToken,
                jobSchedulerFilterSchema);
        Clusters jobscheduler200VSchema = (Clusters) jobschedulerClusterResponse.getEntity();
        assertEquals("postjobschedulerClusterTest", TestEnvWebserviceGlobalsTest.SCHEDULER_ID, jobscheduler200VSchema.getCluster().getJobschedulerId());
    }

}
