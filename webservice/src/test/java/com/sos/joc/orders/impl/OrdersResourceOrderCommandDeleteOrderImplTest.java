package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.OkSchema;
import com.sos.joc.model.order.ModifyOrderSchema;
import com.sos.joc.model.order.ModifyOrdersSchema;

public class OrdersResourceOrderCommandDeleteOrderImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersCommandDeleteOrder() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        ModifyOrdersSchema modifyOrdersSchema = new ModifyOrdersSchema();
        ArrayList<ModifyOrderSchema> orders = new ArrayList<ModifyOrderSchema>();
        ModifyOrderSchema order = new ModifyOrderSchema();
        order.setOrderId("junit_test");
        order.setJobChain("/test/job_chain1");
        orders.add(order);
     
        ModifyOrderSchema order2 = new ModifyOrderSchema();
        order2.setOrderId("junit_test2");
        order2.setState("100");
        order2.setJobChain("/test/job_chain1");
        orders.add(order2);

        modifyOrdersSchema.setOrders(orders);
        modifyOrdersSchema.setJobschedulerId("scheduler_current");
        OrdersResourceCommandDeleteOrderImpl ordersResourceHistoryImpl = new OrdersResourceCommandDeleteOrderImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersDelete(sosShiroCurrentUserAnswer.getAccessToken(), modifyOrdersSchema);
        OkSchema okSchema = (OkSchema) ordersResponse.getEntity();
        assertEquals("postOrdersCommandDeleteOrder",true, okSchema.getOk());
     }

}

