package com.sos.joc.orders.impl;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersOverView;

public class OrdersResourceOverviewSummaryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrdersOverviewSummary() throws Exception {

        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrdersResourceOverviewSummaryImpl ordersResourceOverviewSummaryImpl = new OrdersResourceOverviewSummaryImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(ordersFilterSchema);
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSummaryImpl.postOrdersOverviewSummary(accessToken, b);
        OrdersOverView summarySchema = (OrdersOverView) ordersResponse.getEntity();
//        assertEquals("postOrdersOverviewSummary", 0, summarySchema.getOrders().getFailed().intValue());
    }

}
