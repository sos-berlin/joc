package com.sos.joc.order.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrderV200;
import com.sos.joc.model.order.OrderFilter;

public class OrderResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrderTest() throws Exception {

        OrderFilter orderBody = new OrderFilter();
        orderBody.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        orderBody.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orderBody.setOrderId(TestEnvWebserviceTest.ORDER);
        OrderResourceImpl orderImpl = new OrderResourceImpl();
        JOCDefaultResponse ordersResponse = orderImpl.postOrder(accessToken, orderBody);
        OrderV200 order200VSchema = (OrderV200) ordersResponse.getEntity();
        // System.out.println(order200VSchema.getOrder().toString());
        assertEquals("postOrderTest", TestEnvWebserviceTest.JOB_CHAIN, order200VSchema.getOrder().getJobChain());
    }

}
