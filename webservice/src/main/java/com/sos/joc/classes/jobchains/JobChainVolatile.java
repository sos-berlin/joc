package com.sos.joc.classes.jobchains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.jobs.JobVolatile;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.ConfigurationStateText;
import com.sos.joc.model.jobChain.FileWatchingNodeFile;
import com.sos.joc.model.jobChain.FileWatchingNodeV;
import com.sos.joc.model.jobChain.JobChainNodeJobChainV;
import com.sos.joc.model.jobChain.JobChainNodeJobV;
import com.sos.joc.model.jobChain.JobChainNodeState;
import com.sos.joc.model.jobChain.JobChainNodeStateText;
import com.sos.joc.model.jobChain.JobChainNodeV;
import com.sos.joc.model.jobChain.JobChainState;
import com.sos.joc.model.jobChain.JobChainStateText;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.order.OrderState;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersSummary;


public class JobChainVolatile extends JobChainV {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainVolatile.class);
    private Element jobChain;
    private JOCXmlJobChainCommand jocXmlCommand;
    private NodeList jobNodes = null;
    private NodeList jobChainNodes = null;
    private NodeList blacklist = null;
    private Set<String> nestedJobChains = new HashSet<String>();

    public JobChainVolatile() {
        super();
    }
    
    public JobChainVolatile(Element jobChain, JOCXmlJobChainCommand jocXmlCommand) {
        super();
        this.jobChain = jobChain;
        this.jocXmlCommand = jocXmlCommand;
    }
    
    public Set<String> getNestedJobChains() {
        return nestedJobChains;
    }

    public void setPath() {
        if (getPath() == null) {
            setPath(jobChain.getAttribute(WebserviceConstants.PATH));
            LOGGER.debug("...processing jobChain: " + getPath());
            setInitialOrdersSummary();
        }
    }

    public void setState() throws Exception {
        if (getState() == null) {
            setState(new JobChainState());
            try {
                getState().set_text(JobChainStateText.fromValue(jobChain.getAttribute(WebserviceConstants.STATE).toUpperCase()));
            } catch (Exception e) {
                getState().set_text(JobChainStateText.ACTIVE);
            }
            setSeverity(getState());
        }
    }
    
    public void setFields(boolean compact) throws Exception {
        if (compact) {
            setCompactFields();
            cleanArrays();
        } else {
            setDetailedFields();
        }
    }
    
    public boolean hasJobNodes() {
        return jobNodes.getLength() > 0;
    }
    
    public boolean hasJobChainNodes() {
        return jobChainNodes.getLength() > 0;
    }
    
    public void setOrders(List<OrderV> orders) {
        Map<String,List<OrderV>> nodeMap = new HashMap<String,List<OrderV>>();
        for (OrderV order : orders) {
            String node = order.getState();
            if (!nodeMap.containsKey(node)) {
                nodeMap.put(node, new ArrayList<OrderV>()); 
            }
            nodeMap.get(node).add(order);
        }
        for (JobChainNodeV node : getNodes()) {
            List<OrderV> o = nodeMap.get(node.getName());
            node.setOrders(o);
            node.setNumOfOrders(o == null ? 0 : o.size());
        }
    }
    
    public void setOrders(Map<String, OrderVolatile> orders, Integer maxOrders) {
        Map<String,List<OrderV>> nodeMap = new HashMap<String,List<OrderV>>();
        Map<String,Integer> nodeMapCounter = new HashMap<String,Integer>();
        for (OrderV order : orders.values()) {
            String node = order.getState();
            if (!nodeMap.containsKey(node)) {
                nodeMap.put(node, new ArrayList<OrderV>());
                nodeMapCounter.put(node, 0);
            }
            int incrementCounter = nodeMapCounter.get(node)+1;
            nodeMapCounter.put(node, incrementCounter);
            if (maxOrders == null || nodeMap.get(node).size() < maxOrders) {
                nodeMap.get(node).add(order);
            }
        }
        for (JobChainNodeV node : getNodes()) {
            List<OrderV> o = nodeMap.get(node.getName());
            Integer num = nodeMapCounter.get(node.getName());
            node.setOrders(o);
            node.setNumOfOrders(num == null ? 0 : num);
        }
    }
    
    public void setOuterOrdersAndSummary(Map<String, OrderVolatile> orders, Integer maxOrders, Boolean compact) {
        Map<String,List<OrderV>> nodeMap = new HashMap<String,List<OrderV>>();
        Map<String,Integer> nodeMapCounter = new HashMap<String,Integer>();
        OrdersSummary summary = setInitialOrdersSummary();
        if (orders == null) {
            setNumOfOrders(0);
        } else {
            setNumOfOrders(orders.size());
            for (OrderV order : orders.values()) {
                OrderStateText oStateText = null; 
                if (order.getProcessingState() != null ) {
                    oStateText = order.getProcessingState().get_text();
                }
                if (oStateText == null ) {
                    continue;
                }
                switch (oStateText) {
                case BLACKLIST:
                    summary.setBlacklist(summary.getBlacklist() + 1);
                    break;
                case JOB_CHAIN_STOPPED:
                case JOB_NOT_IN_PERIOD:
                case JOB_STOPPED:
                case NODE_DELAY:
                case NODE_STOPPED:
                case WAITING_FOR_AGENT:
                case WAITING_FOR_LOCK:
                case WAITING_FOR_PROCESS:
                case WAITING_FOR_TASK:
                    summary.setWaitingForResource(summary.getWaitingForResource() + 1);
                    break;
                case PENDING:
                    summary.setPending(summary.getPending() + 1);
                    break;
                case RUNNING:
                    summary.setRunning(summary.getRunning() + 1);
                    break;
                case SETBACK:
                    summary.setSetback(summary.getSetback() + 1);
                    break;
                case SUSPENDED:
                    summary.setSuspended(summary.getSuspended() + 1);
                    break;
                }
                if (compact != null && compact) {
                    continue;
                }
                String node = order.getJobChain();
                if (!nodeMap.containsKey(node)) {
                    nodeMap.put(node, new ArrayList<OrderV>());
                    nodeMapCounter.put(node, 0);
                }
                int incrementCounter = nodeMapCounter.get(node)+1;
                nodeMapCounter.put(node, incrementCounter);
                if (maxOrders == null || nodeMap.get(node).size() < maxOrders) {
                    nodeMap.get(node).add(order);
                }
            }
        }
        
        setOrdersSummary(summary);
        if (compact == null || !compact) {
            for (JobChainNodeV node : getNodes()) {
                List<OrderV> o = nodeMap.get(node.getJobChain().getPath());
                Integer num = nodeMapCounter.get(node.getJobChain().getPath());
                node.setOrders(o);
                node.setNumOfOrders(num == null ? 0 : num);
            }
        }
    }
    
    public OrdersSummary setInitialOrdersSummary() {
        OrdersSummary summary = new OrdersSummary();
        summary.setBlacklist(0);
        summary.setPending(0);
        summary.setRunning(0);
        summary.setSetback(0);
        summary.setSuspended(0);
        summary.setWaitingForResource(0);
        setOrdersSummary(summary);
        return summary;       
    }
    
    public void getProcessingStateText(OrderV order) {
        switch (order.getProcessingState().get_text()) {
        case BLACKLIST:
            break;
        case JOB_CHAIN_STOPPED:
        case JOB_NOT_IN_PERIOD:
        case JOB_STOPPED:
        case NODE_DELAY:
        case NODE_STOPPED:
        case WAITING_FOR_AGENT:
        case WAITING_FOR_LOCK:
        case WAITING_FOR_PROCESS:
        case WAITING_FOR_TASK:
            break;
        case PENDING:
            break;
        case RUNNING:
            break;
        case SETBACK:
            break;
        case SUSPENDED:
            break;
        }
    }

    private void cleanArrays() {
        setNodes(null);
        setFileOrderSources(null);
        if (getBlacklist() != null && getBlacklist().size() == 0) {
            setBlacklist(null);
        }
    }

    private void setDetailedFields() throws Exception {
        setCompactFields();
        setFileOrderSources(getFileWatchingNodeVSchema());
        setNodes(new ArrayList<JobChainNodeV>());
        setBlacklist(getBlacklist(blacklist));
        for (int i=0; i < jobNodes.getLength(); i++) {
            Element jobNodeElem = (Element) jobNodes.item(i);
            JobChainNodeV node = new JobChainNodeV();
            node.setName(jobNodeElem.getAttribute("state"));
            node.setLevel(jobNodeElem.getAttribute("state").replaceAll("[^:]", "").length());
            NodeList ordersOfCurrentNode = jocXmlCommand.getSosxml().selectNodeList(jobNodeElem, "order_queue/order");
            
            //jocXmlCommand.getSosxml().selectSingleNode(jobNodeElem, "order_queue/order");
            //node.setOrders(orders);
            Element jobElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(jobNodeElem, "job");
            if (jobElem != null) {
                JobVolatile jobV = new JobVolatile(jobElem, jocXmlCommand);
                JobChainNodeJobV job = new JobChainNodeJobV();
                job.setPath(jobElem.getAttribute(WebserviceConstants.PATH));
                job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(jobElem));
                //JOC-89
                jobV.setState(hasRunningOrWaitingOrder(ordersOfCurrentNode));
                job.setState(jobV.getState());
                node.setJob(job);
            } else {
                // ISSUE 131: If Job is missing do not throw NPE, rather set state to Severity -> 2, Text -> RESOURCE_IS_MISSING
                JobChainNodeJobV job = new JobChainNodeJobV();
                job.setPath(jobNodeElem.getAttribute("job"));
                ConfigurationState confStatus = new ConfigurationState();
                confStatus.setSeverity(2);
                confStatus.setMessage(ConfigurationStateText.RESOURCE_IS_MISSING.toString());
                job.setConfigurationStatus(confStatus);
                node.setJob(job);
            }
            node.setState(getNodeState(jobNodeElem));
            getNodes().add(node);
        }
        for (int i=0; i < jobChainNodes.getLength(); i++) {
            Element jobChainNodeElem = (Element) jobChainNodes.item(i);
            nestedJobChains.add(jobChainNodeElem.getAttribute("job_chain"));
            JobChainNodeV node = new JobChainNodeV();
            JobChainNodeJobChainV jobChain = new JobChainNodeJobChainV();
            jobChain.setPath(jobChainNodeElem.getAttribute("job_chain"));
            node.setJobChain(jobChain);
            node.setName(jobChainNodeElem.getAttribute("state"));
            node.setLevel(0);
            node.setState(getNodeState(jobChainNodeElem));
            getNodes().add(node);
        }
    }
    
    private boolean hasRunningOrWaitingOrder(NodeList orders) throws Exception {

        // TODO maybe use JsonApi
        if (orders == null) {
            return false;
        }
        for (int i = 0; i < orders.getLength(); i++) {
            Element order = (Element) orders.item(i);
            if (order.hasAttribute("task")) {
                return true;
            }
            if (order.hasAttribute("setback") || order.hasAttribute("suspended") || !order.hasAttribute("touched")) {
                // that's not exact, orders are untouched too, if they
                // waitingForResource at the first node
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    private List<OrderV> getBlacklist(NodeList blacklist) {
        List<OrderV> orders = new ArrayList<OrderV>();
        for (int i = 0; i < blacklist.getLength(); i++) {
            Element blacklistedOrder = (Element) blacklist.item(i);
            OrderV order = new OrderV();
            order.set_type(OrderType.FILE_ORDER);
            order.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(blacklistedOrder));
            order.setHistoryId(blacklistedOrder.getAttribute("history_id"));
            order.setJobChain(blacklistedOrder.getAttribute("job_chain"));
            order.setOrderId(blacklistedOrder.getAttribute("id"));
            order.setPath(order.getJobChain()+","+order.getOrderId());
            OrderState orderState = new OrderState();
            orderState.set_text(OrderStateText.BLACKLIST);
            orderState.setSeverity(3);
            order.setProcessingState(orderState);
            order.setStartedAt(JobSchedulerDate.getDateFromISO8601String(blacklistedOrder.getAttribute("start_time")));
            order.setState(blacklistedOrder.getAttribute("state"));
            order.setParams(null); //TODO set params
            orders.add(order);
        }
        if (orders.size() == 0) {
            return null;
        }
        return orders;
    }

    private void setCompactFields() throws Exception {
        setPath();
        setState();
        setSurveyDate(jocXmlCommand.getSurveyDate());
        setName(jobChain.getAttribute(WebserviceConstants.NAME));
        jobNodes = jocXmlCommand.getSosxml().selectNodeList(jobChain, "job_chain_node[@job and not(@job='/scheduler_file_order_sink')]");
        jobChainNodes = jocXmlCommand.getSosxml().selectNodeList(jobChain, "job_chain_node[@job_chain] | job_chain_node.job_chain[@job_chain]");
        blacklist = jocXmlCommand.getSosxml().selectNodeList(jobChain, "blacklist/order");
        setNumOfNodes(jobNodes.getLength() + jobChainNodes.getLength());
        //Integer numOfFileOrders = Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(jobChain, "file_order_source/files/@count", "0"));
        setNumOfOrders(Integer.parseInt(jobChain.getAttribute("orders")) + blacklist.getLength());
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(jobChain));
        //OrdersSummary is set with OrdersSummaryCallable (look at JOCXmlJobChainCommand.getJobChains)
    }
    
    private List<FileWatchingNodeV> getFileWatchingNodeVSchema() throws Exception {
        NodeList fileOrderSources = jocXmlCommand.getSosxml().selectNodeList(jobChain, "file_order_source");
        if (fileOrderSources.getLength() == 0) {
            return null;
        }
        List<FileWatchingNodeV> fileWatchingNodes = new ArrayList<FileWatchingNodeV>();
        for (int i=0; i < fileOrderSources.getLength(); i++) {
           Element fileOrderSourceElement = (Element) fileOrderSources.item(i);
           FileWatchingNodeV fileOrderSource = new FileWatchingNodeV();
           fileOrderSource.setAlertWhenDirectoryMissing(jocXmlCommand.getBoolValue(fileOrderSourceElement.getAttribute("alert_when_directory_missing"), null));
           fileOrderSource.setDirectory(fileOrderSourceElement.getAttribute("directory"));
           fileOrderSource.setRegex(jocXmlCommand.getAttributeValue(fileOrderSourceElement, "regex", null));
           String repeat = jocXmlCommand.getAttributeValue(fileOrderSourceElement, "repeat", null);
           if (repeat != null) {
               fileOrderSource.setRepeat(Integer.parseInt(repeat));
           }
           String delay = jocXmlCommand.getAttributeValue(fileOrderSourceElement, "delay_after_error", null);
           if (delay != null) {
               fileOrderSource.setDelayAfterError(Integer.parseInt(delay));
           }
           NodeList fileOrders = jocXmlCommand.getSosxml().selectNodeList(fileOrderSourceElement, "files/file");
           if (fileOrders.getLength() > 0) {
               List<FileWatchingNodeFile> files = new ArrayList<FileWatchingNodeFile>();
               for (int j=0; j < fileOrders.getLength(); j++) {
                   Element fileOrder = (Element) fileOrders.item(j);
                   FileWatchingNodeFile file = new FileWatchingNodeFile();
                   file.setPath(fileOrder.getAttribute(WebserviceConstants.PATH));
                   file.setModified(JobSchedulerDate.getDateFromISO8601String(jocXmlCommand.getAttributeValue(fileOrder, "last_write_time", null)));
                   files.add(file);
               }
               fileOrderSource.setFiles(files);
           } else {
               fileOrderSource.setFiles(null);
           }
           fileWatchingNodes.add(fileOrderSource);
        }
        return fileWatchingNodes;
    }
    
    private void setSeverity(JobChainState state) {
        switch (state.get_text()) {
        case ACTIVE:
            state.setSeverity(4);
            break;
        case INITIALIZED:
            state.setSeverity(3);
            break;
        case NOT_INITIALIZED:
        case STOPPED:
        case UNDER_CONSTRUCTION:
            state.setSeverity(2);
            break;
        }
    }
    
    private JobChainNodeState getNodeState(Element elem) {
        JobChainNodeState state = new JobChainNodeState();
        switch(elem.getAttribute("action")) {
        case "stop":
            state.setSeverity(2);
            state.set_text(JobChainNodeStateText.STOPPED);
            return state;
        case "next_state":
            state.setSeverity(5);
            state.set_text(JobChainNodeStateText.SKIPPED);
            return state;
        default:
            state.setSeverity(4);
            state.set_text(JobChainNodeStateText.ACTIVE);
            return state;
        }
    }
}
