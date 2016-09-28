package com.sos.joc.db.inventory.instances;

import static org.junit.Assert.*;

 
import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JobSchedulerIdentifier;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;

public class InventoryDBLayerTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test

    public void getJobSchedulerInstance() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobSchedulerIdentifier jobSchedulerIdentifier = new JobSchedulerIdentifier("scheduler_current");
        SOSShiroCurrentUser sosShiroCurrentUser = Globals.currentUsersList.getUser(sosShiroCurrentUserAnswer.getAccessToken());

         
        InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        sosShiroCurrentUser.addSchedulerInstanceDBItem (jobSchedulerIdentifier,dbLayer.getInventoryInstanceBySchedulerId(jobSchedulerIdentifier.getSchedulerId()));
        DBItemInventoryInstance schedulerInstancesDBItem = sosShiroCurrentUser.getSchedulerInstanceDBItem(jobSchedulerIdentifier);
        
        assertEquals("getJobSchedulerInstance", "http://localhost:4444", schedulerInstancesDBItem.getCommandUrl());

    }

}
