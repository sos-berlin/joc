package com.sos.joc.db.inventory.jobs;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJob;
import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;

public class InventoryJobsDBLayerTest {

    @Before
    public void setUp() throws Exception {
        TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void getJobSchedulerJobs() throws Exception {
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.createSosHibernateStatelessConnection(
                "InventoryInstancesDBLayer"));
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(TestEnvWebserviceTest.SCHEDULER_ID, getAccessToken());
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.createSosHibernateStatelessConnection("InventoryJobsDBLayer"));

        List<DBItemInventoryJob> listOfJobs = dbLayer.getInventoryJobs(instance.getId());
        assertEquals("getJobSchedulerJobs", false, listOfJobs.isEmpty());

    }

    @Test
    public void getJobSchedulerJob() throws Exception {
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.createSosHibernateStatelessConnection(
                "InventoryInstancesDBLayer"));
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(TestEnvWebserviceTest.SCHEDULER_ID, getAccessToken());
        InventoryJobsDBLayer dbLayer = new InventoryJobsDBLayer(Globals.createSosHibernateStatelessConnection("InventoryJobsDBLayer"));

        DBItemInventoryJob job = dbLayer.getInventoryJobByName(TestEnvWebserviceTest.JOB_CHAIN, instance.getId());
        assertEquals("getJobSchedulerJob", TestEnvWebserviceTest.JOB, job.getName());

    }

    private String getAccessToken() {
        return UUID.randomUUID().toString();
    }

}
