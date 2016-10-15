package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;
 
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;

public class OrdersResourceOverviewSnapshotImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersOverviewSnapshot() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        JobChainsFilter filterSchema = new JobChainsFilter();
        filterSchema.setJobschedulerId("scheduler_current");
        OrdersResourceOverviewSnapshotImpl ordersResourceOverviewSnapshotImpl = new OrdersResourceOverviewSnapshotImpl();
        JOCDefaultResponse ordersResponse = ordersResourceOverviewSnapshotImpl.postOrdersOverviewSnapshot(sosShiroCurrentUserAnswer.getAccessToken(), filterSchema);
        OrdersSnapshot snapshotSchema = (OrdersSnapshot) ordersResponse.getEntity();
        assertEquals("postOrdersOverviewSnapshot",-1, snapshotSchema.getOrders().getRunning().intValue());
     }

}

