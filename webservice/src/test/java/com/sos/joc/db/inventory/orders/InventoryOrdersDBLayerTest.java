package com.sos.joc.db.inventory.orders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;

public class InventoryOrdersDBLayerTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test
    public void getOrderschedulerOrders() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler_current");
        InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection);
        List<DBItemInventoryOrder>  listOfOrders = dbLayer.getInventoryOrders(instance.getId());
        assertEquals("getOrderschedulerOrders", "dod_tools/active_ftp_accounts,set_ftp_passwords", listOfOrders.get(0).getName());
    }
    
    
    @Test
    public void getOrderschedulerOrder() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId("scheduler_current");
        InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection);
        DBItemInventoryOrder order = dbLayer.getInventoryOrderByOrderId("dod_tools/active_ftp_accounts", "set_ftp_passwords", instance.getId());
        assertEquals("getOrderschedulerOrder", "active_ftp_accounts,set_ftp_passwords", order.getBaseName());
    }

}