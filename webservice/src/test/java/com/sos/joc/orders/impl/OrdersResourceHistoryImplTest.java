package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.HistorySchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.response.JOCDefaultResponse;

public class OrdersResourceHistoryImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersHistory() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersBody ordersBody = new OrdersBody();
        ordersBody.setJobschedulerId("scheduler_current");
        OrdersResourceHistoryImpl ordersResourceHistoryImpl = new OrdersResourceHistoryImpl();
        JOCDefaultResponse ordersResponse = ordersResourceHistoryImpl.postOrdersHistory(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        HistorySchema historySchema = (HistorySchema) ordersResponse.getEntity();
        assertEquals("postOrdersHistory","myPath", historySchema.getHistory().get(0).getPath());
     }

}

