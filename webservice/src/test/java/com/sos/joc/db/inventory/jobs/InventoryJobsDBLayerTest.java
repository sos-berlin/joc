package com.sos.joc.db.inventory.jobs;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;

public class InventoryJobsDBLayerTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test
    public void getJobSchedulerJobs() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler_current");
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection);
        
        List<DBItemInventoryJob>  listOfJobs = dbLayer.getInventoryJobs(instance.getId());
        
        assertEquals("getJobSchedulerJobs", "batch_install_universal_agent/PerformInstall", listOfJobs.get(0).getName());

    }
    
    
    @Test
    public void getJobSchedulerJob() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler_current");
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.sosHibernateConnection);
        
        DBItemInventoryJob job = dbLayer.getInventoryJobByName("batch_install_universal_agent/PerformInstall", instance.getId());
        
        assertEquals("getJobSchedulerJob", "PerformInstall", job.getBaseName());

    }

    

}
