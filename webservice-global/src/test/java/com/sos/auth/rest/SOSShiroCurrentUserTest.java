package com.sos.auth.rest;

import org.junit.Test;

public class SOSShiroCurrentUserTest {

    @Test
    public void testGetExcluded() {
        String permission = "sos:products:joc_cockpit:jobscheduler_master:execute:continue";
        String masterId = "scheduler_joc_cockpit";

        SOSShiroCurrentUser sosShiroCurrentUser = new SOSShiroCurrentUser("user", "pwd");
        sosShiroCurrentUser.testGetExcluded(permission, masterId);
    }

}
