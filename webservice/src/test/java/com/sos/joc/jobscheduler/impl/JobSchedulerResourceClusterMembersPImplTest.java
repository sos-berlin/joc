package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterMembersPImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.MastersP;

public class JobSchedulerResourceClusterMembersPImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postjobschedulerClusterMembersPTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        JobSchedulerResourceClusterMembersPImpl jobschedulerResourceClusterMembersPImpl = new JobSchedulerResourceClusterMembersPImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(jobSchedulerFilterSchema);
        JOCDefaultResponse jobschedulerClusterMembersPResponse = jobschedulerResourceClusterMembersPImpl.postJobschedulerClusterMembers(accessToken, b);
        MastersP mastersPSchema = (MastersP) jobschedulerClusterMembersPResponse.getEntity();
        assertEquals("postjobschedulerClusterMembersPTest", TestEnvWebserviceTest.SCHEDULER_ID, mastersPSchema.getMasters().get(0).getJobschedulerId());
    }

}
