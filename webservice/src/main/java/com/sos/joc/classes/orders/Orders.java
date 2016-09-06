package com.sos.joc.classes.orders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.job.ProcessingState;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.OrdersSummary;
import com.sos.joc.model.job.OrderQueue.Type;

public class Orders {

    public static List<OrderQueue> getOrderQueueList() {
        
        List<OrderQueue> listOrderQueue = new ArrayList<OrderQueue>();

        OrderQueue orderQueue = new OrderQueue();

        orderQueue.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());

        orderQueue.setEndState("myEndState");
        orderQueue.setHistoryId(-1);
        orderQueue.setInProcessSince(new Date());
        orderQueue.setJob("myJob");
        orderQueue.setJobChain("myJobChain");
        orderQueue.setLock("myLock");
        orderQueue.setNextStartTime(new Date());
        orderQueue.setOrderId("myOrderId");

       
        orderQueue.setParams(Parameters.getParameters());

        orderQueue.setPath("myPath");
        orderQueue.setPriority(-1);
        orderQueue.setProcessClass("myProcessClass");
        orderQueue.setProcessedBy("myProcessedBy");

        ProcessingState processingState = new ProcessingState();
        processingState.setSeverity(1);
        processingState.setText(ProcessingState.Text.RUNNING);

        orderQueue.setProcessingState(processingState);

        orderQueue.setSetback(new Date());
        orderQueue.setStartedAt(new Date());
        orderQueue.setState("myState");
        orderQueue.setStateText("myStateText");
        orderQueue.setSurveyDate(new Date());
        orderQueue.setTaskId(-1);
        orderQueue.setType(Type.FILE_ORDER);
        listOrderQueue.add(orderQueue);
        return listOrderQueue;
    }
    
    public static OrdersSummary getOrdersSummary(){
        OrdersSummary ordersSummary = new OrdersSummary();
        ordersSummary.setPending(-1);
        ordersSummary.setRunning(-1);
        ordersSummary.setSetback(-1);
        ordersSummary.setSuspended(-1);
        ordersSummary.setWaitingForResource(-1);
        return ordersSummary;
    }
}
