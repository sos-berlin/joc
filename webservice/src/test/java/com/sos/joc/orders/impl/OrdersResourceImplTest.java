package com.sos.joc.orders.impl;
 
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.order.OrderPath;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrdersFilter;
import com.sos.joc.model.order.OrdersV;

public class OrdersResourceImplTest {
    private static final String LDAP_PASSWORD = "root";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersTest() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        OrdersV ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","/06_YADEFileTransfer/01_yade_file_transfer", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest2() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder("/webservice");
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        folder2.setRecursive(false);
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest3() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder("/webservice");
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        ordersBody.setRegex("SplitAndSync");
        OrdersV ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest4() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder("/webservice");
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        ordersBody.setRegex("SplitAndSync");
        List<OrderPath> orders = ordersBody.getOrders();
        OrderPath order = new OrderPath();
        order.setJobChain("/webservice/setback");
        order.setOrderId("2");
        orders.add(order);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest5() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        Folder folder = new Folder();
        folder.setFolder("/webservice");
        Folder folder2 = new Folder();
        folder2.setFolder("/examples");
        List<Folder> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        List<OrderType> types = ordersBody.getTypes();
        types.add(OrderType.AD_HOC);
        ordersBody.setTypes(types);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTestWithException() throws Exception   {
         
        OrdersFilter ordersBody = new OrdersFilter();
        List<OrderPath> orders = ordersBody.getOrders();
        OrderPath order = new OrderPath();
        order.setJobChain("/webservice/setbac");
        order.setOrderId("unknown");
        orders.add(order);
        OrdersV ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    private OrdersV TestHelper(OrdersFilter ordersBody) throws Exception {
        ordersBody.setJobschedulerId("scheduler.1.12");
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersResourceImpl ordersImpl = new OrdersResourceImpl();
        JOCDefaultResponse ordersResponse = ordersImpl.postOrders(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        OrdersV ordersVSchema = (OrdersV) ordersResponse.getEntity();
        return ordersVSchema;
    }
       
    @Test
    public void UriBuilder() throws UnsupportedEncodingException {
        System.out.println("http://www.query.example/?key="+URLEncoder.encode("{val}", "UTF-8"));
        System.out.println(UriBuilder.fromPath("http://www.query.exämple/").queryParam("key", "ß").queryParam("key2", "ß").build());
    }
}

