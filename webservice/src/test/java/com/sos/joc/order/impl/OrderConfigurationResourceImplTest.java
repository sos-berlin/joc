package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.order.OrderConfigurationFilter;

public class OrderConfigurationResourceImplTest {

    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    
    @Test
    public void postOrderConfTest() throws Exception   {
         
        OrderConfigurationFilter orderConfigurationBody = new OrderConfigurationFilter();
        orderConfigurationBody.setJobChain(TestEnvWebserviceGlobalsTest.JOB_CHAIN);
        orderConfigurationBody.setOrderId(TestEnvWebserviceGlobalsTest.ORDER);
        orderConfigurationBody.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        OrderConfigurationResourceImpl orderConfigurationImpl = new OrderConfigurationResourceImpl();
        JOCDefaultResponse ordersResponse = orderConfigurationImpl.postOrderConfiguration(accessToken, orderConfigurationBody);
        Configuration200 orderConfigurationSchema = (Configuration200) ordersResponse.getEntity();
        assertEquals("postOrderConfTest",TestEnvWebserviceGlobalsTest.JOB_CHAIN + "," + TestEnvWebserviceGlobalsTest.ORDER, orderConfigurationSchema.getConfiguration().getPath());
     }

}

