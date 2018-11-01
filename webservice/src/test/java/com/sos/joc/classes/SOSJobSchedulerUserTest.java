package com.sos.joc.classes;

import static org.junit.Assert.*;

 
import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;

 
public class SOSJobSchedulerUserTest {
 

    private static final String PASSWORD = "root";
    private static final String USER = "root";

    @Test

    public void getJobSchedulerInstance() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();

        JobSchedulerUser sosJobschedulerUser = new JobSchedulerUser(sosShiroCurrentUserAnswer.getAccessToken());

        DBItemInventoryInstance schedulerInstancesDBItem = sosJobschedulerUser.getSchedulerInstance("scheduler.1.12");
        assertEquals("getJobSchedulerInstance", "http://galadriel:40412", schedulerInstancesDBItem.getUrl());

    }
}
