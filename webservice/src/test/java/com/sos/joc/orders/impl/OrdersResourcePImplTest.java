package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;

public class OrdersResourcePImplTest {

    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";

    @Test
    public void postOrdersPLDAPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer =
                (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId("scheduler_current");
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(sosShiroCurrentUserAnswer.getAccessToken(), ordersFilterSchema);

        OrdersP ordersPSchema = (OrdersP) ordersResponseP.getEntity();
        assertEquals(
                "postjobschedulerClusterTest",
                "RAPID/APAC Solution/QA/QA_TRACE_GENEALOGY/CHN_EVENT_QA_TRACE_GENEALOGY_STAR_CHECK_EVENT,ORD_EVENT_QA_TRACE_GENEALOGY_STAR_CHECK_EVENT",
                ordersPSchema.getOrders().get(0).getPath());
    }

    @Test
    public void postOrdersPTest() throws Exception {

        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer =
                (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId("sp_scheduler_cluster");
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(sosShiroCurrentUserAnswer.getAccessToken(), ordersFilterSchema);
        OrdersP ordersPSchema = (OrdersP) ordersResponseP.getEntity();
    }

}
