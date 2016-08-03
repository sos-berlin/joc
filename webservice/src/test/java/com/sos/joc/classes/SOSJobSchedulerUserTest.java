package com.sos.joc.classes;

import static org.junit.Assert.*;

 
import org.junit.Test;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.scheduler.db.SchedulerInstancesDBItem;

public class SOSJobSchedulerUserTest {
 

    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test

    public void getJobSchedulerInstance() {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();

        JobschedulerUser sosJobschedulerUser = new JobschedulerUser(sosShiroCurrentUserAnswer.getAccessToken());

        SchedulerInstancesDBItem schedulerInstancesDBItem = sosJobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier("scheduler_current"));
        assertEquals("getJobSchedulerInstance", "http://localhost:4000", schedulerInstancesDBItem.getUrl());

    }
}
