package com.sos.joc.classes.job;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.job.JobOrderQueueFilterSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.scheduler.model.commands.JSCmdShowJob;

public class JobOrderQueue {

    public static String createJobOrderQueuePostCommand(final JobOrderQueueFilterSchema body) {
        boolean compact = body.getCompact();
        String jobChainNode = body.getNode();
        // if set => ???
        String orderId = body.getOrderId();
        // if set => ???

        JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
        if (!body.getJob().isEmpty()) {
            // if body.getJob() is set => job_orders AND if compact => task_queue job_params
            // if body.getJobChain() is set => job_chain_orders job_chains AND if compact => task_queue job_params
            if (!compact) {
                if (body.getJobChain() != null && !body.getJobChain().isEmpty()) {
                    showJob.setWhat("job_orders job_chain_orders");
                } else {
                    showJob.setWhat("job_orders");
                }
            } else {
                if (body.getJobChain() != null && !body.getJobChain().isEmpty()) {
                    showJob.setWhat("job_orders task_queue job_params");
                } else {
                    showJob.setWhat("job_orders job_chain_orders job_chains task_queue job_params");
                }
            }
            showJob.setJob(body.getJob());
            showJob.setJobChain(body.getJobChain());
        }
        return Globals.schedulerObjectFactory.toXMLString(showJob);
    }

    public static List<OrderQueue> getOrderQueue(Node orderQueueNode, JOCXmlCommand jocXmlCommand) throws Exception {
        List<OrderQueue> orderQueueList = new ArrayList<OrderQueue>();
        NodeList orders = jocXmlCommand.getSosxml().selectNodeList(orderQueueNode, "//order_queue/order");
        for(int i = 0; i < orders.getLength(); i++) {
            OrderQueue orderQueue = new OrderQueue();
            orderQueue.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus((Element)orders.item(i)));
            orderQueue.setJob(((Element)orders.item(i)).getAttribute(WebserviceConstants.JOB));
            orderQueue.setJobChain(((Element)orders.item(i)).getAttribute(WebserviceConstants.JOB_CHAIN));
            orderQueue.setNextStartTime(JobSchedulerDate.getDate(((Element)orders.item(i)).getAttribute(WebserviceConstants.NEXT_START_TIME)));
            orderQueue.setOrderId(((Element)orders.item(i)).getAttribute(WebserviceConstants.ID));
            orderQueue.setPath(((Element)orders.item(i)).getAttribute(WebserviceConstants.PATH));
            String priority = ((Element)orders.item(i)).getAttribute(WebserviceConstants.PRIORITY);
            if (priority != null && !priority.isEmpty()){
                orderQueue.setPriority(Integer.valueOf(priority));
            }
            orderQueue.setState(((Element)orders.item(i)).getAttribute(WebserviceConstants.STATE));
            orderQueue.setSurveyDate(jocXmlCommand.getSurveyDate());
            // TODO get the EndState for the given Order
//            orderQueue.setEndState(endState);
            // TODO get the historyId for the given Order
//            orderQueue.setHistoryId(historyId);
            // TODO get the inProcessIn for the given Order
//            orderQueue.setInProcessSince(inProcessSince);
            // TODO get the parameters for the given Order
            orderQueue.setParams(null);
            // TODO get the processClass for the given Order
//            orderQueue.setProcessClass(processClass);
            // TODO get processedBy for the given Order
//            orderQueue.setProcessedBy(processedBy);
            // TODO get the processingState for the given Order
//            orderQueue.setProcessingState(processingState);
            // TODO get the setback for the given Order
//            orderQueue.setSetback(setback);
            // TODO get startedAt for the given Order
//            orderQueue.setStartedAt(startedAt);
            // TODO get the StateText for the given Order
//            orderQueue.setStateText(stateText);
            // TODO get the Job Lock blocking the given Order
//            orderQueue.setLock(lock);
            // TODO get the TaskId for the given Order if running
//            orderQueue.setTaskId(taskId);
            // TODO get the Type for the given Order 
//            orderQueue.setType(type); 
            orderQueueList.add(orderQueue);
        }
        if (!orderQueueList.isEmpty()) {
            return orderQueueList;
        } else {
            return null;
        }
    }
}
