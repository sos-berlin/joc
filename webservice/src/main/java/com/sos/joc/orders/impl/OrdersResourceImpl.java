package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import com.sos.auth.classes.JobSchedulerIdentifier;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.order.OrdersVSchema;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.orders.post.OrdersBody;
import com.sos.joc.orders.resource.IOrdersResource;
import com.sos.joc.response.JocCockpitResponse;

@Path("orders")
public class OrdersResourceImpl extends JOCResourceImpl implements IOrdersResource {

    @Override
    public OrdersResponse postOrders(String accessToken, OrdersBody ordersBody) throws Exception {

        OrdersResponse ordersResponse;
        jobschedulerUser = new JobSchedulerUser(accessToken);

        if (jobschedulerUser.isTimedOut()) {
            return OrdersResponse.responseStatus440(JocCockpitResponse.getError401Schema(accessToken));
        }

        if (!jobschedulerUser.isAuthenticated()) {
            return OrdersResponse.responseStatus401(JocCockpitResponse.getError401Schema(jobschedulerUser));
        }

        if (!getPermissons().getJobschedulerUniversalAgent().getView().isStatus()) {
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

            entity.setDeliveryDate(new Date());
            List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();
            OrderQueue orderQueue1 = new OrderQueue();
            OrderQueue orderQueue2 = new OrderQueue();

            orderQueue1.setHistoryId(-1);
            ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
            configurationStatusSchema.setMessage("myMessage");
            configurationStatusSchema.setSeverity(ConfigurationStatusSchema.Severity._2);
            configurationStatusSchema.setText(ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);

            orderQueue1.setConfigurationStatus(configurationStatusSchema);
            orderQueue1.setInProcessSince(new Date());
            orderQueue1.setJob("myJob1");
            orderQueue1.setJobChain("myJobChain1");
            orderQueue1.setLock("myLock1");
            orderQueue1.setNextStartTime(new Date());
            orderQueue1.setOrderId("myOrderId1");

            orderQueue1.setPath("myPath1");
            orderQueue1.setPriority(-1);
            orderQueue1.setProcessClass("myProcessClass1");
            orderQueue1.setProcessedBy("myProcessedBy1");
            ProcessingState processingState = new ProcessingState();
            processingState.setDescription("myDescription1");
            processingState.setSeverity(ProcessingState.Severity._0);
            processingState.setText(ProcessingState.Text.JOB_NOT_IN_PERIOD);
            orderQueue1.setProcessingState(processingState);
            orderQueue1.setSetback(new Date());
            orderQueue1.setStartedAt(new Date());
            orderQueue1.setState("myState1");
            orderQueue1.setStateText("myStateText1");
            orderQueue1.setSurveyDate(new Date());
            orderQueue1.setTaskId(-1);
            orderQueue1.setType(OrderQueue.Type.FILE_ORDER);
            listOrderQueue.add(orderQueue1);

            orderQueue2.setHistoryId(-1);
            configurationStatusSchema.setMessage("myMessage");
            configurationStatusSchema.setSeverity(ConfigurationStatusSchema.Severity._2);
            configurationStatusSchema.setText(ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);
            orderQueue2.setConfigurationStatus(configurationStatusSchema);
            orderQueue2.setInProcessSince(new Date());
            orderQueue2.setJob("myJob2");
            orderQueue2.setJobChain("myJobChain2");
            orderQueue2.setLock("myLock2");
            orderQueue2.setNextStartTime(new Date());
            orderQueue2.setOrderId("myOrderId2");

            orderQueue2.setPath("myPath2");
            orderQueue2.setPriority(-1);
            orderQueue2.setProcessClass("myProcessClass2");
            orderQueue2.setProcessedBy("myProcessedBy2");
            processingState.setDescription("myDescription2");
            processingState.setSeverity(ProcessingState.Severity._1);
            processingState.setText(ProcessingState.Text.JOB_CHAIN_STOPPED);
            orderQueue2.setProcessingState(processingState);
            orderQueue2.setSetback(new Date());
            orderQueue2.setStartedAt(new Date());
            orderQueue2.setState("myState2");
            orderQueue2.setStateText("myStateText2");
            orderQueue2.setSurveyDate(new Date());
            orderQueue2.setTaskId(-1);
            orderQueue2.setType(OrderQueue.Type.AD_HOC);
            listOrderQueue.add(orderQueue1);

            entity.setOrders(listOrderQueue);

            ordersResponse = OrdersResponse.responseStatus200(entity);

            return ordersResponse;
        } catch (Exception e) {

            return OrdersResponse.responseStatus420(JocCockpitResponse.getError420Schema(e.getMessage()));
        }

    }

}
