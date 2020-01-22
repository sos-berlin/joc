package com.sos.joc.order.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.model.order.OrderStepHistory;

public class OrderHistoryResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrderHistoryTest() throws Exception {

        OrderHistoryFilter orderFilterSchema = new OrderHistoryFilter();
        orderFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orderFilterSchema.setOrderId(TestEnvWebserviceTest.ORDER);
        orderFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        orderFilterSchema.setHistoryId(TestEnvWebserviceTest.VALID_HISTORY_ID);
        OrderHistoryResourceImpl orderHistoryImpl = new OrderHistoryResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(orderFilterSchema);
        JOCDefaultResponse ordersResponse = orderHistoryImpl.postOrderHistory(accessToken, b);
        OrderStepHistory stepHistorySchema = (OrderStepHistory) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest", 1, stepHistorySchema.getHistory().getSteps().get(0).getStep().intValue());
    }

}
