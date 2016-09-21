package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.Order200VSchema;
import com.sos.joc.model.order.OrderFilterWithCompactSchema;

public class OrderResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderFilterWithCompactSchema orderBody = new OrderFilterWithCompactSchema();
        orderBody.setJobschedulerId("scheduler.1.10");
        orderBody.setJobChain("/webservice/setback");
        orderBody.setOrderId("1");
        OrderResourceImpl orderImpl = new OrderResourceImpl();
        JOCDefaultResponse ordersResponse = orderImpl.postOrder(sosShiroCurrentUserAnswer.getAccessToken(), orderBody);
        Order200VSchema order200VSchema = (Order200VSchema) ordersResponse.getEntity();
        //System.out.println(order200VSchema.getOrder().toString());
        //assertEquals("postOrderTest",-1, order200VSchema.getOrder().getHistoryId().intValue());
     }

}

