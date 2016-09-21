package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.Order200PSchema;
import com.sos.joc.order.post.OrderBody;

public class OrderPResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderBody orderBody = new OrderBody();
        orderBody.setJobChain("Cluster/cluster/job_chain1");
        orderBody.setOrderId("8");
        orderBody.setJobschedulerId("scheduler_current");
        OrderPResourceImpl orderPImpl = new OrderPResourceImpl();
        JOCDefaultResponse ordersResponse = orderPImpl.postOrderP(sosShiroCurrentUserAnswer.getAccessToken(), orderBody);
        Order200PSchema order200PSchema = (Order200PSchema) ordersResponse.getEntity();
        assertEquals("postOrderPTest","Titel from db", order200PSchema.getOrder().getTitle());
     }

}

