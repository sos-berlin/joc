package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.OkSchema;
import com.sos.joc.orders.post.commands.start.Order;
import com.sos.joc.orders.post.commands.start.OrdersModifyOrderBody;
import com.sos.joc.response.JOCDefaultResponse;

public class OrdersResourceOrderCommandDeleteOrderImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersCommandDeleteOrder() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersModifyOrderBody ordersBody = new OrdersModifyOrderBody();
        ArrayList<Order> orders = new ArrayList<Order>();
        Order order = new Order();
        order.setOrderId("junit_test");
        order.setJobChain("/test/job_chain1");
        orders.add(order);
     
        Order order2 = new Order();
        order2.setOrderId("junit_test2");
        order2.setState("100");
        order2.setJobChain("/test/job_chain1");
        orders.add(order2);

        ordersBody.setOrders(orders);
        ordersBody.setJobschedulerId("scheduler_current");
        OrdersResourceCommandDeleteOrderImpl ordersResourceHistoryImpl = new OrdersResourceCommandDeleteOrderImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersDelete(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        OkSchema okSchema = (OkSchema) ordersResponse.getEntity();
        assertEquals("postOrdersHistory",true, okSchema.getOk());
     }

}

