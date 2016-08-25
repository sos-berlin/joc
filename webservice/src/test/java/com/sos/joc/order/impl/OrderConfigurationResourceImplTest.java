package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.order.post.OrderConfigurationBody;
import com.sos.joc.response.JOCDefaultResponse;

public class OrderConfigurationResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderConfigurationBody orderConfigurationBody = new OrderConfigurationBody();
        orderConfigurationBody.setJobChain("Cluster/cluster/job_chain1");
        orderConfigurationBody.setOrderId("8");
        orderConfigurationBody.setJobschedulerId("scheduler_current");
        OrderConfigurationResourceImpl orderConfigurationImpl = new OrderConfigurationResourceImpl();
        JOCDefaultResponse ordersResponse = orderConfigurationImpl.postOrderConfiguration(sosShiroCurrentUserAnswer.getAccessToken(), orderConfigurationBody);
        ConfigurationSchema orderConfigurationSchema = (ConfigurationSchema) ordersResponse.getEntity();
        assertEquals("postjobschedulerClusterTest","myPath", orderConfigurationSchema.getConfiguration().getPath());
     }

}

