package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.LogContentSchema;
import com.sos.joc.order.post.OrderBody;

public class OrderLogResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderBody orderBody = new OrderBody();
        orderBody.setJobChain("Cluster/cluster/job_chain1");
        orderBody.setOrderId("8");
        orderBody.setJobschedulerId("scheduler_current");
        OrderLogResourceImpl orderLogImpl = new OrderLogResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogImpl.postOrderLog(sosShiroCurrentUserAnswer.getAccessToken(), orderBody);
        LogContentSchema logContentSchema = (LogContentSchema) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest","<html><body>log</body></html>", logContentSchema.getHtml());
     }

}

