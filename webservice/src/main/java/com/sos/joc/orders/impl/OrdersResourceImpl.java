package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCProcessingState;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.orders.post.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResource;
import com.sos.joc.response.JobSchedulerResponse;
import com.sos.joc.response.JocCockpitResponse;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {
    private static final Logger LOGGER = Logger.getLogger(OrdersResourceImpl.class);

    @Override
    public OrdersResponse postOrders(String accessToken, OrdersBody ordersBody) throws Exception {

        OrdersResponse ordersResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        try {
            if (!jobschedulerUser.isAuthenticated()) {
                return OrdersResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
            }
        } catch (org.apache.shiro.session.ExpiredSessionException e) {
            LOGGER.error(e.getMessage());
            return OrdersResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken,e.getMessage()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return OrdersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }


  
        if (!getPermissons().getOrder().getView().isStatus()) {
             return OrdersResponse.responseStatus403(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (ordersBody.getJobschedulerId() == null) {
            return OrdersResponse.responseStatus420(JocCockpitResponse.getError420Schema("schedulerId is null"));
        }

        try {

            DBItemInventoryInstance dbItemInventoryInstance = jobschedulerUser.getSchedulerInstance(new JobSchedulerIdentifier(ordersBody.getJobschedulerId()));

            if (dbItemInventoryInstance == null) {
                return OrdersResponse.responseStatus420(JocCockpitResponse.getError420Schema(String.format("schedulerId %s not found in table %s", ordersBody.getJobschedulerId(),
                        DBLayer.TABLE_INVENTORY_INSTANCES)));
            }
            

            OrdersVSchema entity = new OrdersVSchema();

            // TODO JOC Cockpit Webservice
            String actProcessingState = ProcessingState.Text.PENDING.toString();

            entity.setDeliveryDate(new Date());
            List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();
            OrderQueue orderQueue1 = new OrderQueue();
            OrderQueue orderQueue2 = new OrderQueue();

            orderQueue1.setSurveyDate(new Date());
            orderQueue1.setPath("myPath1");
            orderQueue1.setOrderId("myOrderId1");
            orderQueue1.setJobChain("myJobChain1");
            orderQueue1.setState("myState1");
            orderQueue1.setJob("myJob1");
            
            JOCProcessingState processingState = new JOCProcessingState();
            processingState.setDescription("myDescription1");
            processingState.setSeverity(ProcessingState.Severity._0);
            processingState.setText(ProcessingState.Text.JOB_NOT_IN_PERIOD);
            orderQueue1.setProcessingState(processingState.getProcessingState());

            ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
            configurationStatusSchema.setMessage("myMessage");
            configurationStatusSchema.setSeverity(ConfigurationStatusSchema.Severity._2);
            configurationStatusSchema.setText(ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);

            orderQueue1.setType(OrderQueue.Type.FILE_ORDER);
            orderQueue1.setConfigurationStatus(configurationStatusSchema);
            orderQueue1.setInProcessSince(new Date());

            if (!ordersBody.getCompact()) {
                orderQueue1.setPriority(-1);
                orderQueue1.setStateText("myStateText1");
                // orderQueue1.setEndState("");
                List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                NameValuePairsSchema param1 = new NameValuePairsSchema();
                NameValuePairsSchema param2 = new NameValuePairsSchema();
                param1.setName("param1");
                param1.setValue("value1");
                param2.setName("param2");
                param2.setValue("value2");
                parameters.add(param1);
                parameters.add(param1);
                orderQueue1.setParams(parameters);
            }

            if (processingState.isSetLock(actProcessingState)) {
                orderQueue1.setLock("myLock1");
            }

            if (processingState.isSetNextStartTime(actProcessingState)) {
                orderQueue1.setNextStartTime(new Date());
            }

            if (processingState.isSetHistoryId(actProcessingState)) {
                orderQueue1.setHistoryId(-1);
                orderQueue1.setStartedAt(new Date());
            }

            if (processingState.isSetProcessClass(actProcessingState)) {
                orderQueue1.setProcessClass("myProcessClass1");
            }

            if (processingState.isSetTaskId(actProcessingState)) {
                orderQueue1.setTaskId(-1);
            }

            if (processingState.isSetProcessedBy(actProcessingState)) {
                orderQueue1.setProcessedBy("myProcessedBy1");
            }

            if (processingState.isSetSetBack(actProcessingState)) {
                orderQueue1.setSetback(new Date());
            }

            listOrderQueue.add(orderQueue1);

            orderQueue2.setSurveyDate(new Date());
            orderQueue2.setPath("myPath1");
            orderQueue2.setOrderId("myOrderId1");
            orderQueue2.setJobChain("myJobChain1");
            orderQueue2.setState("myState1");
            orderQueue2.setJob("myJob1");
            orderQueue2.setProcessingState(processingState.getProcessingState());

            orderQueue2.setType(OrderQueue.Type.FILE_ORDER);
            orderQueue2.setConfigurationStatus(configurationStatusSchema);
            orderQueue2.setInProcessSince(new Date());

            if (!ordersBody.getCompact()) {
                orderQueue2.setPriority(-1);
                orderQueue2.setStateText("myStateText1");
                // orderQueue2.setEndState("");
                List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
                NameValuePairsSchema param1 = new NameValuePairsSchema();
                NameValuePairsSchema param2 = new NameValuePairsSchema();
                param1.setName("param1");
                param1.setValue("value1");
                param2.setName("param2");
                param2.setValue("value2");
                parameters.add(param1);
                parameters.add(param1);
                orderQueue2.setParams(parameters);
            }
           
            if (processingState.isSetLock(actProcessingState)) {
                orderQueue2.setLock("myLock2");
            }

            if (processingState.isSetNextStartTime(actProcessingState)) {
                orderQueue2.setNextStartTime(new Date());
            }

            if (processingState.isSetHistoryId(actProcessingState)) {
                orderQueue2.setHistoryId(-1);
                orderQueue2.setStartedAt(new Date());
            }

            if (processingState.isSetProcessClass(actProcessingState)) {
                orderQueue2.setProcessClass("myProcessClass2");
            }

            if (processingState.isSetTaskId(actProcessingState)) {
                orderQueue2.setTaskId(-1);
            }

            if (processingState.isSetProcessedBy(actProcessingState)) {
                orderQueue2.setProcessedBy("myProcessedBy2");
            }

            if (processingState.isSetSetBack(actProcessingState)) {
                orderQueue2.setSetback(new Date());
            }

            listOrderQueue.add(orderQueue2);

            entity.setOrders(listOrderQueue);

            ordersResponse = OrdersResponse.responseStatus200(entity);

            return ordersResponse;
        } catch (Exception e) {

            return OrdersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
