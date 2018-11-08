package com.sos.joc.order.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderP200;
import com.sos.joc.model.order.OrderFilter;

public class OrderPResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postOrderPTest() throws Exception {

        OrderFilter orderFilterWithCompactSchema = new OrderFilter();
        orderFilterWithCompactSchema.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        orderFilterWithCompactSchema.setOrderId(TestEnvWebserviceGlobalsTest.ORDER);
        orderFilterWithCompactSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        OrderPResourceImpl orderPImpl = new OrderPResourceImpl();
        JOCDefaultResponse ordersResponse = orderPImpl.postOrderP(accessToken, orderFilterWithCompactSchema);
        OrderP200 order200PSchema = (OrderP200) ordersResponse.getEntity();
        assertEquals("postOrderPTest", TestEnvWebserviceGlobalsTest.ORDER, order200PSchema.getOrder().getOrderId());
    }

}
