package com.sos.joc.classes;

import static org.junit.Assert.*;

 
import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;

public class SOSJobSchedulerUserTest {
 

    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test

    public void getJobSchedulerInstance() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();

        JobSchedulerUser sosJobschedulerUser = new JobSchedulerUser(sosShiroCurrentUserAnswer.getAccessToken());

        DBItemInventoryInstance schedulerInstancesDBItem = sosJobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier("scheduler_current"));
        assertEquals("getJobSchedulerInstance", "http://localhost:4444", schedulerInstancesDBItem.getCommandUrl());

    }
}
