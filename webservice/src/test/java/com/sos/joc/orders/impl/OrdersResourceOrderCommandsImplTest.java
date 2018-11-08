package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

public class OrdersResourceOrderCommandsImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrdersCommand() throws Exception {

        ModifyOrders modifyOrderSchema = new ModifyOrders();
        ArrayList<ModifyOrder> orders = new ArrayList<ModifyOrder>();
        ModifyOrder order = new ModifyOrder();
        order.setAt("now + 60");
        order.setState("Create");
        order.setOrderId("junit_test");
        order.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orders.add(order);

        ModifyOrder order2 = new ModifyOrder();
        order2.setAt("now + 60");
        order2.setOrderId("test");
        order2.setState("Create");
        order2.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orders.add(order2);

        modifyOrderSchema.setOrders(orders);
        modifyOrderSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrdersResourceCommandModifyOrderImpl ordersResourceHistoryImpl = new OrdersResourceCommandModifyOrderImpl();
        OrdersResourceCommandAddOrderImpl ordersAddResourceHistoryImpl = new OrdersResourceCommandAddOrderImpl();
        JOCDefaultResponse ordersResponse = ordersAddResourceHistoryImpl.postOrdersAdd(accessToken, modifyOrderSchema);
        ordersResponse = ordersResourceHistoryImpl.postOrdersSetState(accessToken, modifyOrderSchema);
        Ok okSchema = (Ok) ordersResponse.getEntity();
        assertEquals("postOrdersCommand", true, okSchema.getOk());
        OrdersResourceCommandDeleteOrderImpl ordersDeleteResourceHistoryImpl = new OrdersResourceCommandDeleteOrderImpl();
        ordersResponse = ordersDeleteResourceHistoryImpl.postOrdersDelete(accessToken, modifyOrderSchema);
        okSchema = (Ok) ordersResponse.getEntity();
        assertEquals("postOrdersCommandDeleteOrder", true, okSchema.getOk());
    }

}
