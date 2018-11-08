package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderHistory;
import com.sos.joc.model.order.OrdersFilter;

public class OrdersResourceHistoryImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }
    
    @Test
    public void postOrdersHistory() throws Exception {
        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrdersResourceHistoryImpl ordersResourceHistoryImpl = new OrdersResourceHistoryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersHistory(accessToken, ordersFilterSchema);
        OrderHistory historySchema = (OrderHistory) ordersResponse.getEntity();
        assertEquals("postOrdersHistory", TestEnvWebserviceTest.JOB_CHAIN, historySchema.getHistory().get(0).getJobChain());
    }

}
