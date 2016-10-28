package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.order.OrderFilter;

public class OrderRuntimeResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderRunTimeTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrderFilter orderFilterSchema = new OrderFilter();
        orderFilterSchema.setJobChain("Cluster/cluster/job_chain1");
        orderFilterSchema.setOrderId("8");
        orderFilterSchema.setJobschedulerId("scheduler_current");
        OrderRunTimeResourceImpl orderRunTimeImpl = new OrderRunTimeResourceImpl();
        JOCDefaultResponse ordersResponse = orderRunTimeImpl.postOrderRunTime(sosShiroCurrentUserAnswer.getAccessToken(), orderFilterSchema);
        RunTime200 orderRunTimeSchema = (RunTime200) ordersResponse.getEntity();
        assertEquals("postOrderRunTimeTest","myRuntime", orderRunTimeSchema.getRunTime().getRunTime());
     }

}

