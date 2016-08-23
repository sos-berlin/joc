package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.SummarySchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.response.JOCDefaultResponse;

public class OrdersResourceOverviewSummaryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersOverviewSummary() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersBody ordersBody = new OrdersBody();
        ordersBody.setJobschedulerId("scheduler_current");
        OrdersResourceOverviewSummaryImpl ordersResourceOverviewSummaryImpl = new OrdersResourceOverviewSummaryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSummaryImpl.postOrdersOverviewSummary(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        SummarySchema summarySchema = (SummarySchema) ordersResponse.getEntity();
        assertEquals("postOrdersOverviewSummary",-1, summarySchema.getOrders().getFailed().intValue());
     }

}

