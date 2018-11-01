package com.sos.joc.db.inventory.jobs;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;

public class InventoryJobsDBLayerTest {
    private static final String PASSWORD = "root";
    private static final String USER = "root";

    @Test
    public void getJobSchedulerJobs() throws Exception {
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.createSosHibernateStatelessConnection("InventoryInstancesDBLayer"));
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler.1.12", getAccessToken());
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.createSosHibernateStatelessConnection("InventoryJobsDBLayer"));
        
        List<DBItemInventoryJob>  listOfJobs = dbLayer.getInventoryJobs(instance.getId());
        
        assertEquals("getJobSchedulerJobs", false, listOfJobs.isEmpty());

    }
    
    
    @Test
    public void getJobSchedulerJob() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.createSosHibernateStatelessConnection("InventoryInstancesDBLayer"));
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler.1.12", getAccessToken());
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.createSosHibernateStatelessConnection("InventoryJobsDBLayer"));
        
        DBItemInventoryJob job = dbLayer.getInventoryJobByName("/sos/dailyplan/CreateDailyPlan", instance.getId());
        
        assertEquals("getJobSchedulerJob", "CreateDailyPlan", job.getBaseName());

    }

    private String getAccessToken() {
        return UUID.randomUUID().toString();
    }

}
