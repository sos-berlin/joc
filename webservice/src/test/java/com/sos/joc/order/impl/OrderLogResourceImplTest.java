package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.order.OrderHistoryFilter;


public class OrderLogResourceImplTest {
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    @Ignore
    public void postOrderHistoryTest() throws Exception   {
         
        OrderHistoryFilter orderFilterWithHistoryIdSchema = new OrderHistoryFilter();
        orderFilterWithHistoryIdSchema.setJobChain(GlobalsTest.JOB_CHAIN);
        orderFilterWithHistoryIdSchema.setOrderId(GlobalsTest.ORDER);
        orderFilterWithHistoryIdSchema.setHistoryId("1320022");
        orderFilterWithHistoryIdSchema.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        OrderLogResourceImpl orderLogImpl = new OrderLogResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogImpl.postOrderLog(accessToken, orderFilterWithHistoryIdSchema);
        LogContent200 logContentSchema = (LogContent200) ordersResponse.getEntity();
        //assertEquals("postOrderHistoryTest","<!DOCT", logContentSchema.getLog().getHtml().substring(0, 6));
     }

}

