package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.order.OrderHistoryFilter;


public class OrderLogResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderHistoryFilter orderFilterWithHistoryIdSchema = new OrderHistoryFilter();
        orderFilterWithHistoryIdSchema.setJobChain("Cluster/cluster/job_chain1");
        orderFilterWithHistoryIdSchema.setOrderId("8");
        orderFilterWithHistoryIdSchema.setJobschedulerId("scheduler_current");
        OrderLogResourceImpl orderLogImpl = new OrderLogResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogImpl.postOrderLog(sosShiroCurrentUserAnswer.getAccessToken(), orderFilterWithHistoryIdSchema);
        LogContent200 logContentSchema = (LogContent200) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest","<html><body>log</body></html>", logContentSchema.getLog().getHtml());
     }

}

