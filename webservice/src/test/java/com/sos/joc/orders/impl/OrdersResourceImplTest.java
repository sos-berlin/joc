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
    
//    @Test
//    public void JSON() {
//        String response = "{\"usedTasks\":[],\"usedNodes\":[{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/webservice/setback\",\"nodeId\":\"start\"},\"orderCount\":2,\"jobPath\":\"/webservice/sleep\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"},{\"errorNodeId\":\"error\",\"nextNodeId\":\"success\",\"nodeKey\":{\"jobChainPath\":\"/webservice/suspend\",\"nodeId\":\"start\"},\"orderCount\":2,\"jobPath\":\"/webservice/sleep\",\"action\":\"process\",\"TYPE\":\"SimpleJob\"}],\"usedJobs\":[{\"isInPeriod\":false,\"usedTaskCount\":0,\"path\":\"/webservice/sleep\",\"state\":\"pending\",\"fileBasedState\":\"active\",\"obstacles\":[{\"plannedAt\":\"2016-09-04T22:00:00Z\",\"TYPE\":\"NoRuntime\"}],\"taskLimit\":2}],\"eventId\":1472933228974000,\"orders\":[{\"sourceType\":\"Permanent\",\"path\":\"/webservice/setback,1\",\"processingState\":{\"at\":\"2016-09-03T08:00:00Z\",\"TYPE\":\"Pending\"},\"nextStepAt\":\"2016-09-03T08:00:00Z\",\"fileBasedState\":\"active\",\"obstacles\":[],\"nodeId\":\"start\"},{\"sourceType\":\"Permanent\",\"path\":\"/webservice/setback,2\",\"processingState\":{\"TYPE\":\"NotPlanned\"},\"fileBasedState\":\"active\",\"obstacles\":[],\"nodeId\":\"start\"},{\"sourceType\":\"Permanent\",\"path\":\"/webservice/suspend,1\",\"processingState\":{\"TYPE\":\"NotPlanned\"},\"fileBasedState\":\"active\",\"obstacles\":[],\"nodeId\":\"start\"},{\"sourceType\":\"AdHoc\",\"path\":\"/webservice/suspend,889\",\"processingState\":{\"TYPE\":\"WaitingForOther\"},\"nextStepAt\":\"1970-01-01T00:00:00Z\",\"fileBasedState\":\"not_initialized\",\"obstacles\":[{\"TYPE\":\"Suspended\"}],\"nodeId\":\"start\"}],\"usedProcessClasses\":[]}";
//        JSONObject obj2 = new JSONObject(response);
//        JSONArray a2 = obj2.names(); 
//        Set<Object> s2 = obj2.keySet();
//        System.out.println(a2);
//        System.out.println(s2);
//        
//        JsonReader rdr = Json.createReader(new StringReader(response));
//        JsonObject obj = rdr.readObject();
//        System.out.println(obj.keySet());
//        System.out.println(obj.values());
//    }
    
    
    @Test
    public void UriBuilder() throws UnsupportedEncodingException {
        System.out.println("http://www.query.example/?key="+URLEncoder.encode("{val}", "UTF-8"));
        System.out.println(UriBuilder.fromPath("http://www.query.exämple/").queryParam("key", "ß").queryParam("key2", "ß").build());
    }
}

