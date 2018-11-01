package com.sos.joc.order.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.OrderP200;
import com.sos.joc.model.order.OrderFilter;

public class OrderPResourceImplTest {
    private static final String PASSWORD = "root";
    private static final String USER = "root";
     
    @Test
    public void postOrderPTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
        OrderFilter orderFilterWithCompactSchema = new OrderFilter();
        orderFilterWithCompactSchema.setJobChain("sos/dailyplan/CreateDailyPlan");
        orderFilterWithCompactSchema.setOrderId("createDailyPlan");
        orderFilterWithCompactSchema.setJobschedulerId("scheduler.1.12");
        OrderPResourceImpl orderPImpl = new OrderPResourceImpl();
        JOCDefaultResponse ordersResponse = orderPImpl.postOrderP(sosShiroCurrentUserAnswer.getAccessToken(), orderFilterWithCompactSchema);
        OrderP200 order200PSchema = (OrderP200) ordersResponse.getEntity();
        assertEquals("postOrderPTest","dailyplan", order200PSchema.getOrder().getTitle());
     }

}

