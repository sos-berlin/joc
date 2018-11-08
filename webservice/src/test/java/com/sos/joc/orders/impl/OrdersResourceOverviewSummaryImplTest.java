package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersOverView;

public class OrdersResourceOverviewSummaryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postOrdersOverviewSummary() throws Exception {

        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        OrdersResourceOverviewSummaryImpl ordersResourceOverviewSummaryImpl = new OrdersResourceOverviewSummaryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSummaryImpl.postOrdersOverviewSummary(accessToken, ordersFilterSchema);
        OrdersOverView summarySchema = (OrdersOverView) ordersResponse.getEntity();
//        assertEquals("postOrdersOverviewSummary", 0, summarySchema.getOrders().getFailed().intValue());
    }

}
