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
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.order.Order_;
import com.sos.joc.model.order.OrdersFilterSchema;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.model.order.Type;

public class OrdersResourceImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrdersTest() throws Exception   {
         
        OrdersFilterSchema ordersBody = new OrdersFilterSchema();
        OrdersVSchema ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest2() throws Exception   {
         
        OrdersFilterSchema ordersBody = new OrdersFilterSchema();
        FoldersSchema folder = new FoldersSchema();
        folder.setFolder("/webservice");
        FoldersSchema folder2 = new FoldersSchema();
        folder2.setFolder("/examples");
        folder2.setRecursive(false);
        List<FoldersSchema> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        OrdersVSchema ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest3() throws Exception   {
         
        OrdersFilterSchema ordersBody = new OrdersFilterSchema();
        FoldersSchema folder = new FoldersSchema();
        folder.setFolder("/webservice");
        FoldersSchema folder2 = new FoldersSchema();
        folder2.setFolder("/examples");
        List<FoldersSchema> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        ordersBody.setRegex("SplitAndSync");
        OrdersVSchema ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest4() throws Exception   {
         
        OrdersFilterSchema ordersBody = new OrdersFilterSchema();
        FoldersSchema folder = new FoldersSchema();
        folder.setFolder("/webservice");
        FoldersSchema folder2 = new FoldersSchema();
        folder2.setFolder("/examples");
        List<FoldersSchema> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        ordersBody.setRegex("SplitAndSync");
        List<Order_> orders = ordersBody.getOrders();
        Order_ order = new Order_();
        order.setJobChain("/webservice/setback");
        order.setOrderId("2");
        orders.add(order);
        OrdersVSchema ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTest5() throws Exception   {
         
        OrdersFilterSchema ordersBody = new OrdersFilterSchema();
        FoldersSchema folder = new FoldersSchema();
        folder.setFolder("/webservice");
        FoldersSchema folder2 = new FoldersSchema();
        folder2.setFolder("/examples");
        List<FoldersSchema> folders = ordersBody.getFolders();
        folders.add(folder);
        folders.add(folder2);
        List<Type> types = ordersBody.getType();
        types.add(Type.AD_HOC);
        ordersBody.setType(types);
        OrdersVSchema ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    @Test
    public void postOrdersTestWithException() throws Exception   {
         
        OrdersFilterSchema ordersBody = new OrdersFilterSchema();
        List<Order_> orders = ordersBody.getOrders();
        Order_ order = new Order_();
        order.setJobChain("/webservice/setbac");
        order.setOrderId("unknown");
        orders.add(order);
        OrdersVSchema ordersVSchema = TestHelper(ordersBody);
        //assertEquals("postOrdersTest","myJob1", ordersVSchema.getOrders().get(0).getJob());
    }
    
    private OrdersVSchema TestHelper(OrdersFilterSchema ordersBody) throws Exception {
        ordersBody.setJobschedulerId("scheduler.1.10");
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginGet("", LDAP_USER, LDAP_PASSWORD).getEntity();
        OrdersResourceImpl ordersImpl = new OrdersResourceImpl();
        JOCDefaultResponse ordersResponse = ordersImpl.postOrders(sosShiroCurrentUserAnswer.getAccessToken(), ordersBody);
        OrdersVSchema ordersVSchema = (OrdersVSchema) ordersResponse.getEntity();
        return ordersVSchema;
    }
       
    @Test
    public void UriBuilder() throws UnsupportedEncodingException {
        System.out.println("http://www.query.example/?key="+URLEncoder.encode("{val}", "UTF-8"));
        System.out.println(UriBuilder.fromPath("http://www.query.exämple/").queryParam("key", "ß").queryParam("key2", "ß").build());
    }
}

