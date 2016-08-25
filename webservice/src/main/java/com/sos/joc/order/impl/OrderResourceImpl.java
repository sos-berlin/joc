package com.sos.joc.order.impl;

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
 
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.ConfigurationStatusSchema.Text;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.OrderQueue.Type;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.model.order.Order200VSchema;
import com.sos.joc.order.post.OrderBody;
import com.sos.joc.order.resource.IOrderResource;
import com.sos.joc.response.JOCDefaultResponse;

@Path("order")
public class OrderResourceImpl extends JOCResourceImpl implements IOrderResource {
    private static final Logger LOGGER = Logger.getLogger(OrderResourceImpl.class);
 
    @Override
    public JOCDefaultResponse postOrder(String accessToken, OrderBody orderBody) throws Exception {
        LOGGER.debug("init Order");
        JOCDefaultResponse jocDefaultResponse = init(orderBody.getJobschedulerId(),getPermissons(accessToken).getOrder().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
 
        try {
 
            Order200VSchema entity = new Order200VSchema();
            OrderQueue orderQueue = new OrderQueue();
             

            entity.setDeliveryDate(new Date());
            ConfigurationStatusSchema configurationStatus = new ConfigurationStatusSchema();
            configurationStatus.setMessage("myMessage");
            configurationStatus.setSeverity(0);
            configurationStatus.setText(Text.changed_file_not_loaded);
            orderQueue.setConfigurationStatus(configurationStatus);
            
            orderQueue.setEndState("myEndState");
            orderQueue.setHistoryId(-1);
            orderQueue.setInProcessSince(new Date());
            orderQueue.setJob("myJob");
            orderQueue.setJobChain("myJobChain");
            orderQueue.setLock("myLock");
            orderQueue.setNextStartTime(new Date());
            orderQueue.setOrderId("myOrderId");
           
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
 
            orderQueue.setPath("myPath");
            orderQueue.setPriority(-1);
            orderQueue.setProcessClass("myProcessClass");
            orderQueue.setProcessedBy("myProcessedBy");
            
            ProcessingState processingState = new ProcessingState();
            processingState.setSeverity(1);
            processingState.setText(ProcessingState.Text.running);
            
            orderQueue.setProcessingState(processingState);
           
            orderQueue.setSetback(new Date());
            orderQueue.setStartedAt(new Date());
            orderQueue.setState("myState");
            orderQueue.setStateText("myStateText");
            orderQueue.setSurveyDate(new Date());
            orderQueue.setTaskId(-1);
            orderQueue.setType(Type.file_order);
             

            entity.setOrder(orderQueue);
            
       
            // TODO JOC Cockpit Webservice

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            System.out.println(e.getCause() + ":" + e.getMessage());
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }

    }


}
