package com.sos.joc.order.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.order.OrderFilter;

public class OrderRuntimeResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrderRunTimeTest() throws Exception {

        OrderFilter orderFilterSchema = new OrderFilter();
        orderFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orderFilterSchema.setOrderId(TestEnvWebserviceTest.ORDER);
        orderFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrderRunTimeResourceImpl orderRunTimeImpl = new OrderRunTimeResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(orderFilterSchema);
        JOCDefaultResponse ordersResponse = orderRunTimeImpl.postOrderRunTime(accessToken, b);
        RunTime200 orderRunTimeSchema = (RunTime200) ordersResponse.getEntity();
        assertTrue("postOrderRunTimeTest", orderRunTimeSchema.getRunTime().getRunTimeXml().startsWith("<run_time"));
    }

}
