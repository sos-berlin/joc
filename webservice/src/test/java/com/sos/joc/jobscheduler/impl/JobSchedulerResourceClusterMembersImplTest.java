package com.sos.joc.jobscheduler.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.jobscheduler.impl.JobSchedulerResourceClusterMembersImpl;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.MastersV;

public class JobSchedulerResourceClusterMembersImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postjobschedulerClusterMembersTest() throws Exception {

        JobSchedulerId jobSchedulerFilterSchema = new JobSchedulerId();
        jobSchedulerFilterSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        JobSchedulerResourceClusterMembersImpl jobschedulerResourceClusterMembersImpl = new JobSchedulerResourceClusterMembersImpl();
        JOCDefaultResponse jobschedulerClusterMembersResponse = jobschedulerResourceClusterMembersImpl.postJobschedulerClusterMembers(accessToken,
                jobSchedulerFilterSchema);
        MastersV mastersVSchema = (MastersV) jobschedulerClusterMembersResponse.getEntity();
        assertEquals("postjobschedulerClusterMembersTest", GlobalsTest.SCHEDULER_ID, mastersVSchema.getMasters().get(0)
                .getJobschedulerId());
    }

}
