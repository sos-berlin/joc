package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
 
public class OrderLogHtmlResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderHistoryTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
         
        OrderLogHtmlResourceImpl orderLogHtmlImpl = new OrderLogHtmlResourceImpl();
        JOCDefaultResponse ordersResponse = orderLogHtmlImpl.getOrderLogHtml(sosShiroCurrentUserAnswer.getAccessToken(), "scheduler_current");
        String logContentSchema = (String) ordersResponse.getEntity();
        assertEquals("postOrderHistoryTest","<html><body>myLog</body></html>", logContentSchema);
     }

}

