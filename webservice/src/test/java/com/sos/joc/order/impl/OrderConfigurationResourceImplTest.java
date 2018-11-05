package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.order.OrderConfigurationFilter;

public class OrderConfigurationResourceImplTest {

    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    
    @Test
    public void postOrderConfTest() throws Exception   {
         
        OrderConfigurationFilter orderConfigurationBody = new OrderConfigurationFilter();
        orderConfigurationBody.setJobChain(GlobalsTest.JOB_CHAIN);
        orderConfigurationBody.setOrderId(GlobalsTest.ORDER);
        orderConfigurationBody.setJobschedulerId(GlobalsTest.SCHEDULER_ID);
        OrderConfigurationResourceImpl orderConfigurationImpl = new OrderConfigurationResourceImpl();
        JOCDefaultResponse ordersResponse = orderConfigurationImpl.postOrderConfiguration(accessToken, orderConfigurationBody);
        Configuration200 orderConfigurationSchema = (Configuration200) ordersResponse.getEntity();
        assertEquals("postOrderConfTest",GlobalsTest.JOB_CHAIN + "," + GlobalsTest.ORDER, orderConfigurationSchema.getConfiguration().getPath());
     }

}

