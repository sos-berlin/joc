package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.order.OrderConfigurationFilterSchema;

public class OrderConfigurationResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderConfTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderConfigurationFilterSchema orderConfigurationBody = new OrderConfigurationFilterSchema();
        orderConfigurationBody.setJobChain("/webservice/setback");
        orderConfigurationBody.setOrderId("1");
        orderConfigurationBody.setJobschedulerId("scheduler.1.10");
        OrderConfigurationResourceImpl orderConfigurationImpl = new OrderConfigurationResourceImpl();
        JOCDefaultResponse ordersResponse = orderConfigurationImpl.postOrderConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), orderConfigurationBody);
        ConfigurationSchema orderConfigurationSchema = (ConfigurationSchema) ordersResponse.getEntity();
        assertEquals("postOrderConfTest","/webservice/setback,1", orderConfigurationSchema.getConfiguration().getPath());
     }

}

