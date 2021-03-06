package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.junit.Before;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersV;

public class OrdersResourceImplTest {
    
    private String accessToken;
    
    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

     
    @Test
    public void postOrdersTest2() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceTest.JOB_CHAIN_FOLDER);
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        folder2.setRecursive(false);
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        assertEquals("postOrdersTest",TestEnvWebserviceTest.JOB, ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest3() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceTest.JOB_CHAIN_FOLDER);
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        ordersBody.setRegex("Create");
        OrdersV ordersVSchema = TestHelper(ordersBody);
        assertEquals("postOrdersTest",TestEnvWebserviceTest.JOB, ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest4() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceTest.JOB_CHAIN_FOLDER);
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        ordersBody.setRegex("Create");
        List<OrderPath> orders = ordersBody.getOrders();
        OrderPath order = new OrderPath();
        order.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        order.setOrderId(TestEnvWebserviceTest.ORDER);
        orders.add(order);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        assertEquals("postOrdersTest",TestEnvWebserviceTest.JOB, ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest5() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder(TestEnvWebserviceTest.JOB_CHAIN_FOLDER);
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        List<OrderType> types = ordersBody.getTypes();
        types.add(OrderType.PERMANENT);
        ordersBody.setTypes(types);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        assertEquals("postOrdersTest",TestEnvWebserviceTest.JOB, ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTestWithException() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        List<OrderPath> orders = ordersBody.getOrders();
        OrderPath order = new OrderPath();
        order.setJobChain(TestEnvWebserviceTest.JOB_CHAIN);
        order.setOrderId(TestEnvWebserviceTest.ORDER);
        orders.add(order);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        assertEquals("postOrdersTest",TestEnvWebserviceTest.JOB, ordersVSchema.getOrders().get(0).getJob());
    }
    
    private OrdersV TestHelper(OrdersFilter ordersBody) throws Exception {
        ordersBody.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        OrdersResourceImpl ordersImpl = new OrdersResourceImpl();
        byte[] b = Globals.objectMapper.writeValueAsBytes(ordersImpl);
        JOCDefaultResponse ordersResponse = ordersImpl.postOrders(accessToken, b);
        OrdersV ordersVSchema = (OrdersV) ordersResponse.getEntity();
        return ordersVSchema;
    }
       
    @Test
    public void UriBuilder() throws UnsupportedEncodingException {
        System.out.println("http://www.query.example/?key="+URLEncoder.encode("{val}", "UTF-8"));
        System.out.println(UriBuilder.fromPath("http://www.query.ex�mple/").queryParam("key", "�").queryParam("key2", "�").build());
    }
}

