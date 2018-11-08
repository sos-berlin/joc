package com.sos.joc.orders.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.TestEnvWebserviceGlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;

public class OrdersResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceGlobalsTest.getAccessToken();
    }

    @Test
    public void postOrdersPLDAPTest() throws Exception {

        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceGlobalsTest.JOB_CHAIN_FOLDER);
        folders.add(folder);
        ordersFilterSchema.setFolders(folders);
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(accessToken, ordersFilterSchema);

        OrdersP ordersPSchema = (OrdersP) ordersResponseP.getEntity();
        assertEquals("postjobschedulerClusterTest", TestEnvWebserviceGlobalsTest.getOrderPath(), ordersPSchema.getOrders().get(0).getPath());
    }

    @Test
    public void postOrdersPTest() throws Exception {

        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceGlobalsTest.SCHEDULER_ID);
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(accessToken, ordersFilterSchema);
        OrdersP ordersPSchema = (OrdersP) ordersResponseP.getEntity();
    }

}
