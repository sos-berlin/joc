package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.order.post.OrderRunTimeBody;

public class OrderRuntimeResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderRunTimeTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderRunTimeBody orderRunTimeBody = new OrderRunTimeBody();
        orderRunTimeBody.setJobChain("Cluster/cluster/job_chain1");
        orderRunTimeBody.setOrderId("8");
        orderRunTimeBody.setJobschedulerId("scheduler_current");
        OrderRunTimeResourceImpl orderRunTimeImpl = new OrderRunTimeResourceImpl();
        JOCDefaultResponse ordersResponse = orderRunTimeImpl.postOrderRunTime(sosShiroCurrentUserAnswer.getAccessToken(), orderRunTimeBody);
        Runtime200Schema orderRunTimeSchema = (Runtime200Schema) ordersResponse.getEntity();
        assertEquals("postOrderRunTimeTest","myRuntime", orderRunTimeSchema.getRunTime().getRunTime());
     }

}

