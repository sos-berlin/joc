package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersOverView;

public class OrdersResourceOverviewSummaryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersOverviewSummary() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId("scheduler_current");
        OrdersResourceOverviewSummaryImpl ordersResourceOverviewSummaryImpl = new OrdersResourceOverviewSummaryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSummaryImpl.postOrdersOverviewSummary(sosShiroCurrentUserAnswer.getAccessToken(), ordersFilterSchema);
        OrdersOverView summarySchema = (OrdersOverView) ordersResponse.getEntity();
        assertEquals("postOrdersOverviewSummary",-1, summarySchema.getOrders().getFailed().intValue());
     }

}

