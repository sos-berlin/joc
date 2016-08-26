package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersPSchema;
import com.sos.joc.orders.post.orders.OrdersBody;

public class OrdersResourcePImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersBody ordersBody = new OrdersBody();
        ordersBody.setJobschedulerId("scheduler_current");
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        
        OrdersPSchema ordersPSchema = (OrdersPSchema) ordersResponseP.getEntity();
        assertEquals("postjobschedulerClusterTest","RAPID/APAC Solution/QA/QA_TRACE_GENEALOGY/CHN_EVENT_QA_TRACE_GENEALOGY_STAR_CHECK_EVENT,ORD_EVENT_QA_TRACE_GENEALOGY_STAR_CHECK_EVENT", ordersPSchema.getOrders().get(0).getPath());
     }

}

