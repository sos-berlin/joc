package com.sos.joc.order.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.model.order.OrderStepHistory;

public class OrderHistoryResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postOrderHistoryTest() throws Exception {

        OrderHistoryFilter orderFilterSchema = new OrderHistoryFilter();
        orderFilterSchema.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        orderFilterSchema.setOrderId(TestEnvWebserviceGlobalsTest.ORDER);
        orderFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        orderFilterSchema.setHistoryId(TestEnvWebserviceGlobalsTest.VALID_HISTORY_ID);
        OrderHistoryResourceImpl orderHistoryImpl = new OrderHistoryResourceImpl();
        JOCDefaultResponse ordersResponse = orderHistoryImpl.postOrderHistory(accessToken, orderFilterSchema);
        OrderStepHistory stepHistorySchema = (OrderStepHistory) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest", 1, stepHistorySchema.getHistory().getSteps().get(0).getStep().intValue());
    }

}
