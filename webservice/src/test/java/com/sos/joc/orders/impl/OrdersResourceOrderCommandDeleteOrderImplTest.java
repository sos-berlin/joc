package com.sos.joc.orders.impl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

public class OrdersResourceOrderCommandDeleteOrderImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrdersCommandDeleteOrder() throws Exception {

        ModifyOrders modifyOrdersSchema = new ModifyOrders();
        ArrayList<ModifyOrder> orders = new ArrayList<ModifyOrder>();
        ModifyOrder order = new ModifyOrder();
        order.setAt("now + 60");
        order.setOrderId("junit_test");
        order.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orders.add(order);

        ModifyOrder order2 = new ModifyOrder();
        order2.setOrderId("junit_test2");
        order2.setAt("now + 60");
        order2.setState("Create");
        order2.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orders.add(order2);

        modifyOrdersSchema.setOrders(orders);

        modifyOrdersSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrdersResourceCommandDeleteOrderImpl ordersDeleteResourceHistoryImpl = new OrdersResourceCommandDeleteOrderImpl();
        OrdersResourceCommandAddOrderImpl ordersAddResourceHistoryImpl = new OrdersResourceCommandAddOrderImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(modifyOrdersSchema);
        JOCDefaultResponse ordersResponse = ordersAddResourceHistoryImpl.postOrdersAdd(accessToken, b);
        ordersResponse = ordersDeleteResourceHistoryImpl.postOrdersDelete(accessToken, b);
        Ok okSchema = (Ok) ordersResponse.getEntity();
        assertEquals("postOrdersCommandDeleteOrder", true, okSchema.getOk());
    }

}
