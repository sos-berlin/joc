package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.SummarySchema;

public class OrdersResourceOverviewSummaryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersOverviewSummary() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersFilterSchema ordersFilterSchema = new OrdersFilterSchema();
        ordersFilterSchema.setJobschedulerId("scheduler_current");
        OrdersResourceOverviewSummaryImpl ordersResourceOverviewSummaryImpl = new OrdersResourceOverviewSummaryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSummaryImpl.postOrdersOverviewSummary(sosShiroCurrentUserAnswer.getAccessToken(), ordersFilterSchema);
        SummarySchema summarySchema = (SummarySchema) ordersResponse.getEntity();
        assertEquals("postOrdersOverviewSummary",-1, summarySchema.getOrders().getFailed().intValue());
     }

}

