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
 
public class OrdersResourceOrderCommandAddOrderImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersCommandAddOrder() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifyOrders modifyOrderSchema = new ModifyOrders();
        ArrayList<ModifyOrder> orders = new ArrayList<ModifyOrder>();
        ModifyOrder order = new ModifyOrder();
        order.setOrderId("junit_test1");
        order.setJobChain("/test/job_chain1");
        orders.add(order);
     
        ModifyOrder order2 = new ModifyOrder();
        order2.setOrderId("junit_test1");
        order2.setState("200");
        order2.setJobChain("/test/job_chain1");
        orders.add(order2);

        modifyOrderSchema.setOrders(orders);
        modifyOrderSchema.setJobschedulerId("scheduler_current");
        OrdersResourceCommandAddOrderImpl ordersResourceHistoryImpl = new OrdersResourceCommandAddOrderImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersAdd(sosShiroCurrentUserAnswer.getAccessToken(), modifyOrderSchema);
        Ok okSchema = (Ok) ordersResponse.getEntity();
        assertEquals("postOrdersCommandAddOrder",true, okSchema.getOk());
     }

}

