package com.sos.joc.order.impl;
 
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
 
public class OrderLogHtmlResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderLogHtmlTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
         
        OrderLogResourceImpl orderLogHtmlImpl = new OrderLogResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogHtmlImpl.getOrderLogHtml(sosShiroCurrentUserAnswer.getAccessToken(), "", "scheduler_current","orderId", "jobChain","0");
        String logContentSchema = (String) ordersResponse.getEntity();
        assertEquals("postOrderLogHtmlTest","<html><body>myLog</body></html>", logContentSchema);
     }

}

