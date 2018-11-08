package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
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
    public void postOrderRunTimeTest() throws Exception   {
         
        OrderFilter orderFilterSchema = new OrderFilter();
        orderFilterSchema.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        orderFilterSchema.setOrderId(TestEnvWebserviceTest.ORDER);
        orderFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrderRunTimeResourceImpl orderRunTimeImpl = new OrderRunTimeResourceImpl();
        JOCDefaultResponse ordersResponse = orderRunTimeImpl.postOrderRunTime(accessToken, orderFilterSchema);
        RunTime200 orderRunTimeSchema = (RunTime200) ordersResponse.getEntity();
        assertEquals("postOrderRunTimeTest","<run_time>\r\n" + 
                "        <weekdays>\r\n" + 
                "            <day day=\"1 2 3 4 5 6 7\">\r\n" + 
                "                <period single_start=\"00:01\"/>\r\n" + 
                "            </day>\r\n" + 
                "        </weekdays>\r\n" + 
                "    </run_time>", orderRunTimeSchema.getRunTime().getRunTime());
     }

}

