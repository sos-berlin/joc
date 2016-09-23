package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.Order200PSchema;
import com.sos.joc.model.order.OrderFilterWithCompactSchema;

public class OrderPResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderFilterWithCompactSchema orderFilterWithCompactSchema = new OrderFilterWithCompactSchema();
        orderFilterWithCompactSchema.setJobChain("Cluster/cluster/job_chain1");
        orderFilterWithCompactSchema.setOrderId("8");
        orderFilterWithCompactSchema.setJobschedulerId("scheduler_current");
        OrderPResourceImpl orderPImpl = new OrderPResourceImpl();
        JOCDefaultResponse ordersResponse = orderPImpl.postOrderP(sosShiroCurrentUserAnswer.getAccessToken(), orderFilterWithCompactSchema);
        Order200PSchema order200PSchema = (Order200PSchema) ordersResponse.getEntity();
        assertEquals("postOrderPTest","Titel from db", order200PSchema.getOrder().getTitle());
     }

}

