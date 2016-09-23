package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.LogContentSchema;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;


public class OrderLogResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema = new OrderFilterWithHistoryIdSchema();
        orderFilterWithHistoryIdSchema.setJobChain("Cluster/cluster/job_chain1");
        orderFilterWithHistoryIdSchema.setOrderId("8");
        orderFilterWithHistoryIdSchema.setJobschedulerId("scheduler_current");
        OrderLogResourceImpl orderLogImpl = new OrderLogResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogImpl.postOrderLog(sosShiroCurrentUserAnswer.getAccessToken(), orderFilterWithHistoryIdSchema);
        LogContentSchema logContentSchema = (LogContentSchema) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest","<html><body>log</body></html>", logContentSchema.getHtml());
     }

}

