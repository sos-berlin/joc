package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.order.AddedOrders;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

public class OrdersResourceOrderCommandAddOrderImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postOrdersCommandAddOrder() throws Exception {

        ModifyOrders modifyOrderSchema = new ModifyOrders();
        ArrayList<ModifyOrder> orders = new ArrayList<ModifyOrder>();
        ModifyOrder order = new ModifyOrder();
        order.setOrderId("junit_test1");
        order.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        orders.add(order);

        ModifyOrder order2 = new ModifyOrder();
        order2.setOrderId("junit_test1");
        order2.setState("Create");
        order2.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        orders.add(order2);

        modifyOrderSchema.setOrders(orders);
        modifyOrderSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        OrdersResourceCommandAddOrderImpl ordersResourceHistoryImpl = new OrdersResourceCommandAddOrderImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersAdd(accessToken, modifyOrderSchema);
        AddedOrders okSchema = (AddedOrders) ordersResponse.getEntity();
        assertEquals("postOrdersCommandAddOrder", true, okSchema.getOk());
    }

}
