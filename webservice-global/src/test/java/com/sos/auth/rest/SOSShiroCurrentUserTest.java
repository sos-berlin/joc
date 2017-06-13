package com.sos.auth.rest;

import static org.junit.Assert.*;

import org.junit.Test;

public class SOSShiroCurrentUserTest {

    @Test
    public void testGetExcluded() {
        String permission = "sos:products:joc_cockpit:jobscheduler_master:execute:continue";
        String permissionMaster = "scheduler_joc_cockpit:sos:products:joc_cockpit:jobscheduler_master:execute:continue";

        SOSShiroCurrentUser sosShiroCurrentUser = new SOSShiroCurrentUser("user", "pwd");
        sosShiroCurrentUser.testGetExcluded(permission, permissionMaster);
    }

}
