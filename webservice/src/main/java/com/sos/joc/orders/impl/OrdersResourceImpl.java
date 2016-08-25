package com.sos.joc.orders.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.jitl.restclient.JobSchedulerRestClient;
import com.sos.joc.classes.JOCProcessingState;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.UsedTasks;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.orders.post.orders.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResource;
import com.sos.joc.response.JOCDefaultResponse;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceImpl.class);
 
    @Override
    public JOCDefaultResponse postOrders(String accessToken, OrdersBody ordersBody) throws Exception {
        LOGGER.debug("init Orders");
        JOCDefaultResponse jocDefaultResponse = init(ordersBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

 
        try {
 
            OrdersVSchema entity = new OrdersVSchema();
            List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();

            entity.setDeliveryDate(new Date());
            
            String actProcessingState = "";
            
           //TODO Use correct url
            JobSchedulerRestClient.headers.put("Content-Type", "application/json");

            String response = JobSchedulerRestClient.executeRestServiceCommand("get", "http://localhost:4404/jobscheduler/master/api/order/?return=OrdersComplemented");

            JsonReader rdr = Json.createReader(new StringReader(response));
            JsonObject obj = rdr.readObject();
            
      
            JsonArray resultUsedTasks = obj.getJsonArray("usedTasks");
            UsedTasks usedTasks = new UsedTasks();
            usedTasks.addEntries(resultUsedTasks);

            JsonArray resultsUsedNodes = obj.getJsonArray("usedNodes");
            for (JsonObject result : resultsUsedNodes.getValuesAs(JsonObject.class)) {
                
            }
            
            JsonArray resultsUsedJobs = obj.getJsonArray("usedJobs");
            for (JsonObject result : resultsUsedJobs.getValuesAs(JsonObject.class)) {
                
            }
            

            JsonArray resultOrders = obj.getJsonArray("orders");
            for (JsonObject order : resultOrders.getValuesAs(JsonObject.class)) {
                
                OrderQueue orderQueue = new OrderQueue();
                JOCProcessingState processingState = new JOCProcessingState();

                orderQueue.setSurveyDate(getDateFromTimestamp(obj.getJsonNumber( "eventId").longValue()));
                orderQueue.setPath(order.getString("path", ""));
                String[] s = order.getString("path", "").split(",");
                orderQueue.setOrderId(s[1]);
                orderQueue.setJobChain(s[0]);
                orderQueue.setState(order.getString("nodeId", ""));
                
                orderQueue.setStartedAt(new Date());
                
                try {
                    actProcessingState = order.getJsonString("processingState").getString();
                    processingState.setText(actProcessingState);
                } catch (Exception e) {
                    actProcessingState = order.getJsonObject("processingState").getString("TYPE");
                    String taskId = order.getJsonObject("processingState").getString("taskId","");
                    processingState.setText(actProcessingState);
                    if (processingState.isSetTaskId(actProcessingState)) {
                        orderQueue.setInProcessSince(getDateFromString(order.getJsonObject("processingState").getString("since")));
                    }
                    if (processingState.isSetProcessClass(actProcessingState)) {
                        orderQueue.setProcessClass(order.getJsonObject("processingState").getString("processClassPath"));
                    }
                    if (processingState.isSetTaskId(actProcessingState)) {
                        orderQueue.setTaskId(Integer.parseInt(taskId));
                    }
                    if (usedTasks.get(taskId) != null){
                        orderQueue.setJob(usedTasks.get(taskId).getJobPath());
                    }
                    if (order.getJsonObject("processingState").getJsonString("processClassPath")  != null){
                       orderQueue.setProcessClass(order.getJsonObject("processingState").getString("processClassPath"));
                    }

                }
                
                
                if (processingState.isSetHistoryId(actProcessingState)) {
                    orderQueue.setHistoryId(-1);
                }

                orderQueue.setProcessingState(processingState.getProcessingState());

                ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
                configurationStatusSchema.setMessage("myMessage");
                configurationStatusSchema.setSeverity(2);
                configurationStatusSchema.setText(ConfigurationStatusSchema.Text.changed_file_not_loaded);
                orderQueue.setConfigurationStatus(configurationStatusSchema);
                
                try { 
                    orderQueue.setType(OrderQueue.Type.fromValue(order.getString("sourceType", "")));
                
                }catch (Exception e){
                    
                }
                
                
                if (!ordersBody.getCompact()) {
                    orderQueue.setPriority(-1);
                    orderQueue.setStateText("myStateText1");
                    //orderQueue1.setEndState("");
                    List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                    NameValuePairsSchema param1 = new NameValuePairsSchema();
                    NameValuePairsSchema param2 = new NameValuePairsSchema();
                    param1.setName("param1");
                    param1.setValue("value1");
                    param2.setName("param2");
                    param2.setValue("value2");
                    parameters.add(param1);
                    parameters.add(param1);
                    orderQueue.setParams(parameters);
                }

                if (processingState.isSetLock(actProcessingState)) {
                    orderQueue.setLock("myLock1");
                }

                if (processingState.isSetNextStartTime(actProcessingState)) {
                    orderQueue.setNextStartTime(getDateFromString(order.getString("nextStepAt", "")));
                }

                if (processingState.isSetProcessedBy(actProcessingState)) {
                    orderQueue.setProcessedBy("myProcessedBy1");
                }

                if (processingState.isSetSetBack(actProcessingState)) {
                    orderQueue.setSetback(new Date());
                }

                listOrderQueue.add(orderQueue);                
                
                
                 JsonArray obstacles = order.getJsonArray("obstacles");
                if (obstacles != null) {
                    for (JsonValue obstacle : obstacles) {
                        System.out.println(obstacle.toString());
                    }
                }
            }

  
            // TODO JOC Cockpit Webservice

        

            entity.setOrders(listOrderQueue);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
