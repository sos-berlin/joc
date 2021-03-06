package com.sos.joc.orders.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersP;

public class OrdersResourcePImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrdersPLDAPTest() throws Exception {

        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceTest.JOB_CHAIN_FOLDER);
        folders.add(folder);
        ordersFilterSchema.setFolders(folders);
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(ordersFilterSchema);
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(accessToken, b);

        OrdersP ordersPSchema = (OrdersP) ordersResponseP.getEntity();
        assertEquals("postjobschedulerClusterTest", TestEnvWebserviceTest.getOrderPath(), ordersPSchema.getOrders().get(0).getPath());
    }

    @Test
    public void postOrdersPTest() throws Exception {

        OrdersFilter ordersFilterSchema = new OrdersFilter();
        ordersFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrdersResourcePImpl ordersPImpl = new OrdersResourcePImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(ordersFilterSchema);
        JOCDefaultResponse ordersResponseP = ordersPImpl.postOrdersP(accessToken, b);
        OrdersP ordersPSchema = (OrdersP) ordersResponseP.getEntity();
    }

}
