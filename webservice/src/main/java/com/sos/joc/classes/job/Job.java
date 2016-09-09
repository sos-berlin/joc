package com.sos.joc.classes.job;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.jobs.JobsUtils;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.Job_;
import com.sos.joc.model.job.Job__;
import com.sos.joc.model.job.State_;
import com.sos.joc.model.job.State__;


public class Job {

    public static Job_ getJob_(Node jobNode, JOCXmlCommand jocXmlCommand, Boolean compact) throws Exception {
        Job_ job = new Job_();
        String value = jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "queued_tasks/@length");
        if (value != null) {
            job.setNumOfQueuedTasks(Integer.valueOf(value));
        } else {
            job.setNumOfQueuedTasks(0);
        }
        
        NodeList lockNodes = jocXmlCommand.getSosxml().selectNodeList(jobNode, "lock.requestor/lock.use");
        job.setLocks(JobsUtils.getLocks_(lockNodes));

        NamedNodeMap attributes = jobNode.getAttributes();
        job.setName(attributes.getNamedItem(WebserviceConstants.NAME).getNodeValue());
        job.setPath(attributes.getNamedItem(WebserviceConstants.PATH).getNodeValue());

        String stateValue = attributes.getNamedItem(WebserviceConstants.STATE).getNodeValue();
        job.setState(JobsUtils.getOutputState_(stateValue, jobNode));
        String stateText = ((Element)jobNode).getAttribute("state_text");
        if(stateText != null && !stateText.isEmpty()) {
            job.setStateText(stateText);
        }

        // BIG TODO
//        OrdersSummary ordersSummary = new OrdersSummary();
//        ordersSummary.setPending(-1);
//        ordersSummary.setRunning(-1);
//        ordersSummary.setSetback(-1);
//        ordersSummary.setSuspended(-1);
//        ordersSummary.setWaitingForResource(-1);
//        job.setOrdersSummary(ordersSummary);

        if (jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count") != null &&
                !"".equalsIgnoreCase(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count"))) {
            job.setNumOfRunningTasks(Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count")));
        } else {
            job.setNumOfRunningTasks(0);
        }

        job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus((Element)jobNode));
        
        if (!compact) {
            job.setAllSteps(Integer.valueOf(attributes.getNamedItem("all_steps").getNodeValue()));
            job.setAllTasks(Integer.valueOf(attributes.getNamedItem("all_tasks").getNodeValue()));
            
            // TODO: job.setDelayUntil(new Date());
//            job.setDelayUntil(new Date());
            
            // TODO: Joacim
//            job.setNextPeriodBegin("myNextPeriodBegin");
            Node namedItem = attributes.getNamedItem(WebserviceConstants.NEXT_START_TIME);
            if(namedItem != null) {
                job.setNextStartTime(JobSchedulerDate.getDate(namedItem.getNodeValue()));
            }

            NodeList paramsNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "params/param");
            List<NameValuePairsSchema> params = Parameters.getParameters(paramsNodes);
            if(params != null && !params.isEmpty()) {
                job.setParams(params);
            }

            NodeList queuedTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "queued_tasks/queued_task");
            job.setTaskQueue(JobsUtils.getQueuedTasks(queuedTasksNodes));
            
            if (job.getNumOfRunningTasks() != null && job.getNumOfRunningTasks() > 0) {
                NodeList runningTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "tasks/task");
                job.setRunningTasks(JobsUtils.getRunningTasks(runningTasksNodes, jocXmlCommand));
            }
            job.setTemporary(false);
        }
        return job;
    }
    
    public static Job__ getJob__(Node jobNode, JOCXmlCommand jocXmlCommand, Boolean compact) throws Exception {
        Job__ job = new Job__();
        String value = jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "queued_tasks/@length");
        if (value != null) {
            job.setNumOfQueuedTasks(Integer.valueOf(value));
        } else {
            job.setNumOfQueuedTasks(0);
        }
        
        NodeList lockNodes = jocXmlCommand.getSosxml().selectNodeList(jobNode, "lock.requestor/lock.use");
        job.setLocks(JobsUtils.getLocks__(lockNodes));

        NamedNodeMap attributes = jobNode.getAttributes();
        job.setName(attributes.getNamedItem(WebserviceConstants.NAME).getNodeValue());
        job.setPath(attributes.getNamedItem(WebserviceConstants.PATH).getNodeValue());

        String stateValue = attributes.getNamedItem(WebserviceConstants.STATE).getNodeValue();
        job.setState(JobsUtils.getOutputState__(stateValue, jobNode));
        String stateText = ((Element)jobNode).getAttribute("state_text");
        if(stateText != null && !stateText.isEmpty()) {
            job.setStateText(stateText);
        }

        // BIG TODO
//        OrdersSummary ordersSummary = new OrdersSummary();
//        ordersSummary.setPending(-1);
//        ordersSummary.setRunning(-1);
//        ordersSummary.setSetback(-1);
//        ordersSummary.setSuspended(-1);
//        ordersSummary.setWaitingForResource(-1);
//        job.setOrdersSummary(ordersSummary);
        
        Node orderQueueNode = jocXmlCommand.getSosxml().selectSingleNode("//job/order_queue");
        job.setOrderQueue(JobOrderQueue.getOrderQueue(orderQueueNode, jocXmlCommand));
        
        if (jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count") != null &&
                !"".equalsIgnoreCase(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count"))) {
            job.setNumOfRunningTasks(Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue((Element)jobNode, "tasks/@count")));
        } else {
            job.setNumOfRunningTasks(0);
        }

        job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus((Element)jobNode));
        
        if (!compact) {
            job.setAllSteps(Integer.valueOf(attributes.getNamedItem("all_steps").getNodeValue()));
            job.setAllTasks(Integer.valueOf(attributes.getNamedItem("all_tasks").getNodeValue()));
            
            // TODO: job.setDelayUntil(new Date());
//            job.setDelayUntil(new Date());
            
            // TODO: Joacim
//            job.setNextPeriodBegin("myNextPeriodBegin");
            // only if job is pending
//            Node namedItem = attributes.getNamedItem(WebserviceConstants.NEXT_START_TIME);
//            if(namedItem != null) {
//                job.setNextStartTime(JobSchedulerDate.getDate(namedItem.getNodeValue()));
//            }
//
            NodeList paramsNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "params/param");
            List<NameValuePairsSchema> params = Parameters.getParameters(paramsNodes);
            if(params != null && !params.isEmpty()) {
                job.setParams(params);
            }

            NodeList queuedTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "queued_tasks/queued_task");
            job.setTaskQueue(JobsUtils.getQueuedTasks(queuedTasksNodes));
            
            if (job.getNumOfRunningTasks() != null && job.getNumOfRunningTasks() > 0) {
                NodeList runningTasksNodes = jocXmlCommand.getSosxml().selectNodeList((Element)jobNode, "tasks/task");
                job.setRunningTasks(JobsUtils.getRunningTasks(runningTasksNodes, jocXmlCommand));
            }
            job.setTemporary(false);
        }
        return job;
    }
    
}
