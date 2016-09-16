package com.sos.joc.db.inventory.jobs;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;

public class InventoryJobsDBLayerTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test
    public void getJobSchedulerJobs() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
         
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection, "scheduler_current");
        
        List<DBItemInventoryJob>  listOfJobs = dbLayer.getInventoryJobs();
        
        assertEquals("getJobSchedulerJobs", "batch_install_universal_agent/PerformInstall", listOfJobs.get(0).getName());

    }
    
    
    @Test
    public void getJobSchedulerJob() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
         
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection, "scheduler_current");
        
        DBItemInventoryJob job = dbLayer.getInventoryJobByName("batch_install_universal_agent/PerformInstall");
        
        assertEquals("getJobSchedulerJob", "PerformInstall", job.getBaseName());

    }

    

}
