package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

public class OrdersResourceOrderCommandDeleteOrderImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersCommandDeleteOrder() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifyOrders modifyOrdersSchema = new ModifyOrders();
        ArrayList<ModifyOrder> orders = new ArrayList<ModifyOrder>();
        ModifyOrder order = new ModifyOrder();
        order.setOrderId("junit_test");
        order.setJobChain("/test/job_chain1");
        orders.add(order);
     
        ModifyOrder order2 = new ModifyOrder();
        order2.setOrderId("junit_test2");
        order2.setState("100");
        order2.setJobChain("/test/job_chain1");
        orders.add(order2);

        modifyOrdersSchema.setOrders(orders);
        modifyOrdersSchema.setJobschedulerId("scheduler_current");
        OrdersResourceCommandDeleteOrderImpl ordersResourceHistoryImpl = new OrdersResourceCommandDeleteOrderImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersDelete(sosShiroCurrentUserAnswer.getAccessToken(), modifyOrdersSchema);
        Ok okSchema = (Ok) ordersResponse.getEntity();
        assertEquals("postOrdersCommandDeleteOrder",true, okSchema.getOk());
     }

}

