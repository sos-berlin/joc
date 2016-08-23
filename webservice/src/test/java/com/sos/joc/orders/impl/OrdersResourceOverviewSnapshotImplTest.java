package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.SnapshotSchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.response.JOCDefaultResponse;

public class OrdersResourceOverviewSnapshotImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersOverviewSnapshot() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersBody ordersBody = new OrdersBody();
        ordersBody.setJobschedulerId("scheduler_current");
        OrdersResourceOverviewSnapshotImpl ordersResourceOverviewSnapshotImpl = new OrdersResourceOverviewSnapshotImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSnapshotImpl.postOrdersOverviewSnapshot(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        SnapshotSchema snapshotSchema = (SnapshotSchema) ordersResponse.getEntity();
        assertEquals("postOrdersOverviewSnapshot",-1, snapshotSchema.getOrders().getRunning().intValue());
     }

}

