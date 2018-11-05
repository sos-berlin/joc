package com.sos.joc.order.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;

public class OrderLogHtmlResourceImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postOrderLogHtmlTest() throws Exception {

        OrderLogResourceImpl orderLogHtmlImpl = new OrderLogResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogHtmlImpl.getOrderLogHtml(accessToken, "", GlobalsTest.SCHEDULER_ID, GlobalsTest.ORDER, GlobalsTest.JOB_CHAIN, "0", null);
        String logContentSchema = (String) ordersResponse.getEntity();
        assertEquals("postOrderLogHtmlTest", "<!DOCT", logContentSchema.substring(0, 6));
    }

}
