package com.sos.joc.db.inventory.orders;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.db.inventory.agents.AgentClusterPermanent;
import com.sos.joc.db.inventory.agents.InventoryAgentsDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;

public class InventoryOrdersDBLayerTest {
    
     
    @Before
    public void setUp() throws Exception {
         TestEnvWebserviceGlobalsTest.getAccessToken();
    }

	@Test
	public void getOrderschedulerOrders() throws Exception {
		SOSHibernateSession sosHibernateSession = null;
		sosHibernateSession = Globals.createSosHibernateStatelessConnection("getOrderschedulerOrders");

		InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(sosHibernateSession);
		DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID,
				getAccessToken());
		InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(sosHibernateSession);
		List<DBItemInventoryOrder> listOfOrders = dbLayer.getInventoryOrders(instance.getId());
		assertTrue("getOrderschedulerOrders",listOfOrders.size()>0);
		sosHibernateSession.close();
	}

	@Test
	public void getOrderschedulerOrder() throws Exception {
		SOSHibernateSession sosHibernateSession = null;
		sosHibernateSession = Globals.createSosHibernateStatelessConnection("getOrderschedulerOrder");
 
		InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(sosHibernateSession);
		DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID,
				getAccessToken());
		InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(sosHibernateSession);
		DBItemInventoryOrder order = dbLayer.getInventoryOrderByOrderId(TestEnvWebserviceGlobalsTest.JOB_CHAIN,
		        TestEnvWebserviceGlobalsTest.ORDER, instance.getId());
		assertEquals("getOrderschedulerOrder", TestEnvWebserviceGlobalsTest.JOB_CHAIN + "," + TestEnvWebserviceGlobalsTest.ORDER, order.getName());
		sosHibernateSession.close();
	}

	private String getAccessToken() {
		return UUID.randomUUID().toString();
	}

	@Test
	public void getAgentCluster() throws Exception {
		SOSHibernateSession sosHibernateSession = null;

		try {
			sosHibernateSession = Globals.createSosHibernateStatelessConnection("getAgentCluster");
 
			InventoryAgentsDBLayer layer = new InventoryAgentsDBLayer(sosHibernateSession);
			sosHibernateSession.beginTransaction();
			List<AgentClusterPermanent> result = layer.getInventoryAgentClusters(12L, null);
			ObjectMapper mapper = new ObjectMapper();
			for (AgentClusterPermanent row : result) {
				System.out.println(row.getSurveyDate());
				System.out.println(mapper.writeValueAsString(row));
			}
		} finally {
			Globals.disconnect(sosHibernateSession);
		}
	}

	@Test
	public void isEndNode() throws Exception {
		SOSHibernateSession sosHibernateSession = null;
		sosHibernateSession = Globals.createSosHibernateStatelessConnection("isEndNode");

		InventoryJobChainsDBLayer layer = new InventoryJobChainsDBLayer(sosHibernateSession);
		sosHibernateSession.beginTransaction();
		boolean result = layer.isEndNode("/reporting/Reporting", "success", 12L);
		sosHibernateSession.close();
		System.out.println(result);
	}
}