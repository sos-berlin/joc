package com.sos.joc.db.inventory.orders;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.agents.AgentClusterPermanent;
import com.sos.joc.db.inventory.agents.InventoryAgentsDBLayer;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;

public class InventoryOrdersDBLayerTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void getOrderschedulerOrders() throws Exception {
        SOSHibernateConnection connection = null;
        connection = Globals.createSosHibernateStatelessConnection("getOrderschedulerOrders");
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler_current", getAccessToken());
        InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
        List<DBItemInventoryOrder> listOfOrders = dbLayer.getInventoryOrders(instance.getId());
        assertEquals("getOrderschedulerOrders", "dod_tools/active_ftp_accounts,set_ftp_passwords", listOfOrders.get(0).getName());
        connection.disconnect();
    }

    @Test
    public void getOrderschedulerOrder() throws Exception {
        SOSHibernateConnection connection = null;
        connection = Globals.createSosHibernateStatelessConnection("getOrderschedulerOrder");
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(connection);
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler_current", getAccessToken());
        InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
        DBItemInventoryOrder order = dbLayer.getInventoryOrderByOrderId("dod_tools/active_ftp_accounts", "set_ftp_passwords", instance.getId());
        assertEquals("getOrderschedulerOrder", "active_ftp_accounts,set_ftp_passwords", order.getBaseName());
        connection.disconnect();
    }

    private String getAccessToken() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void getAgentCluster() throws Exception {
        SOSHibernateConnection connection = null;
        connection = Globals.createSosHibernateStatelessConnection("getAgentCluster");

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryAgentsDBLayer layer = new InventoryAgentsDBLayer(connection);
        connection.beginTransaction();
        List<AgentClusterPermanent> result = layer.getInventoryAgentClusters(12L, null);
        connection.disconnect();
        ObjectMapper mapper = new ObjectMapper();
        for (AgentClusterPermanent row : result) {
            System.out.println(row.getSurveyDate());
            System.out.println(mapper.writeValueAsString(row));
        }
    }

    @Test
    public void isEndNode() throws Exception {
        SOSHibernateConnection connection = null;
        connection = Globals.createSosHibernateStatelessConnection("isEndNode");

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryJobChainsDBLayer layer = new InventoryJobChainsDBLayer(connection);
        connection.beginTransaction();
        boolean result = layer.isEndNode("/reporting/Reporting", "success", 12L);
        connection.disconnect();
        System.out.println(result);
    }
}