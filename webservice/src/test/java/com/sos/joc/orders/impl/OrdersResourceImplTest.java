package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.orders.post.orders.OrdersBody;

public class OrdersResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersBody ordersBody = new OrdersBody();
        ordersBody.setJobschedulerId("scheduler_current");
        OrdersResourceImpl ordersImpl = new OrdersResourceImpl();
        JOCDefaultResponse ordersResponse = ordersImpl.postOrders(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        OrdersVSchema ordersVSchema = (OrdersVSchema) ordersResponse.getEntity();
        assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
     }

}

