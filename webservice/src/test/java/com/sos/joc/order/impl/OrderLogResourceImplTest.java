package com.sos.joc.order.impl;
 
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.order.OrderHistoryFilter;


public class OrderLogResourceImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    @Ignore
    public void postOrderHistoryTest() throws Exception   {
         
        OrderHistoryFilter orderFilterWithHistoryIdSchema = new OrderHistoryFilter();
        orderFilterWithHistoryIdSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orderFilterWithHistoryIdSchema.setOrderId(TestEnvWebserviceTest.ORDER);
        orderFilterWithHistoryIdSchema.setHistoryId("1320022");
        orderFilterWithHistoryIdSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrderLogResourceImpl orderLogImpl = new OrderLogResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(orderFilterWithHistoryIdSchema);
        JOCDefaultResponse ordersResponse = orderLogImpl.postOrderLog(accessToken, b);
        LogContent200 logContentSchema = (LogContent200) ordersResponse.getEntity();
        //assertEquals("postOrderHistoryTest","<!DOCT", logContentSchema.getLog().getHtml().substring(0, 6));
     }

}

