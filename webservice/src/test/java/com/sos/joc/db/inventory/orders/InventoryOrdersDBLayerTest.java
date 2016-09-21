package com.sos.joc.db.inventory.orders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;

public class InventoryOrdersDBLayerTest {
    private static final String LDAP_PASSWORD = "sos01";
    private static final String LDAP_USER = "SOS01";

    @Test
    public void getOrderschedulerOrders() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
         
        InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection, "scheduler_current");
        
        List<DBItemInventoryOrder>  listOfOrders = dbLayer.getInventoryOrders();
        
        assertEquals("getOrderschedulerOrders", "dod_tools/active_ftp_accounts,set_ftp_passwords", listOfOrders.get(0).getName());

    }
    
    
    @Test
    public void getOrderschedulerOrder() throws Exception {
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        
        InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(Globals.sosHibernateConnection, "scheduler_current");
        
        DBItemInventoryOrder order = dbLayer.getInventoryOrderByOrderId("dod_tools/active_ftp_accounts", "set_ftp_passwords");
        
        assertEquals("getOrderschedulerOrder", "active_ftp_accounts,set_ftp_passwords", order.getBaseName());

    }

    

}
