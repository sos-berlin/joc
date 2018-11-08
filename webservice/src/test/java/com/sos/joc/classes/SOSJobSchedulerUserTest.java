package com.sos.joc.classes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.TestEnvWebserviceTest;

public class SOSJobSchedulerUserTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void getJobSchedulerInstance() throws Exception {

        JobSchedulerUser sosJobschedulerUser = new JobSchedulerUser(accessToken);

        DBItemInventoryInstance schedulerInstancesDBItem = sosJobschedulerUser.getSchedulerInstance(TestEnvWebserviceTest.SCHEDULER_ID);
        assertEquals("getJobSchedulerInstance", "http://galadriel:40412", schedulerInstancesDBItem.getUrl());

    }

}
