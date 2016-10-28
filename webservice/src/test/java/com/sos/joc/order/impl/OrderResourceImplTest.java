package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.OrderV200;
import com.sos.joc.model.order.OrderFilter;

public class OrderResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderFilter orderBody = new OrderFilter();
        orderBody.setJobschedulerId("scheduler.1.10");
        orderBody.setJobChain("/webservice/setback");
        orderBody.setOrderId("1");
        OrderResourceImpl orderImpl = new OrderResourceImpl();
        JOCDefaultResponse ordersResponse = orderImpl.postOrder(sosShiroCurrentUserAnswer.getAccessToken(), orderBody);
        OrderV200 order200VSchema = (OrderV200) ordersResponse.getEntity();
        //System.out.println(order200VSchema.getOrder().toString());
        //assertEquals("postOrderTest",-1, order200VSchema.getOrder().getHistoryId().intValue());
     }

}

