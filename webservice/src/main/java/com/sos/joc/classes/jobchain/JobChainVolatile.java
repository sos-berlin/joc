package com.sos.joc.classes.jobchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.jobChain.FileWatchingNodeFile;
import com.sos.joc.model.jobChain.FileWatchingNodeV;
import com.sos.joc.model.jobChain.JobChainNodeJobV;
import com.sos.joc.model.jobChain.JobChainNodeState;
import com.sos.joc.model.jobChain.JobChainNodeStateText;
import com.sos.joc.model.jobChain.JobChainNodeV;
import com.sos.joc.model.jobChain.JobChainState;
import com.sos.joc.model.jobChain.JobChainStateText;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.order.OrderV;


public class JobChainVolatile extends JobChainV {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainVolatile.class);
    private Element jobChain;
    private JOCXmlJobChainCommand jocXmlCommand;
    private NodeList jobNodes = null;
    private NodeList jobChainNodes = null;

    public JobChainVolatile() {
        super();
    }
    
    public JobChainVolatile(Element jobChain, JOCXmlJobChainCommand jocXmlCommand) {
        super();
        this.jobChain = jobChain;
        this.jocXmlCommand = jocXmlCommand;
    }
    
    public void setPath() {
        if (getPath() == null) {
            setPath(jobChain.getAttribute(WebserviceConstants.PATH));
            LOGGER.info("...processing jobChain: " + getPath());
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

    private void cleanArrays() {
        setNodes(null);
        setFileOrderSources(null);
    }

    private void setDetailedFields() throws Exception {
        setCompactFields();
        setFileOrderSources(getFileWatchingNodeVSchema());
        setNodes(new ArrayList<JobChainNodeV>());
        for (int i=0; i < jobNodes.getLength(); i++) {
            Element jobNodeElem = (Element) jobNodes.item(i);
            JobChainNodeV node = new JobChainNodeV();
            node.setName(jobNodeElem.getAttribute("state"));
//            JobV job = new JobV((Element) jocXmlCommand.getSosxml().selectSingleNode(jobNodeElem, "job"), jocXmlCommand);
//            job.setPath(jobNodeElem.getAttribute(WebserviceConstants.PATH));
//            job.setState();
//            job.setLocks(null);
//            job.setOrderQueue(null);
//            job.setParams(null);
//            job.setRunningTasks(null);
//            job.setTaskQueue(null);
            Element jobElem = (Element) jocXmlCommand.getSosxml().selectSingleNode(jobNodeElem, "job");
            JobChainNodeJobV job = new JobChainNodeJobV();
            job.setPath(jobElem.getAttribute(WebserviceConstants.PATH));
            job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(jobElem));
            //TODO
            job.setState(null);
            node.setJob(job);
            node.setState(getNodeState(jobNodeElem));
            getNodes().add(node);
        }
        //TODO fill new nested Job chains array
        for (int i=0; i < jobChainNodes.getLength(); i++) {
            Element jobChainElem = (Element) jobChainNodes.item(i);
            JobChainNodeV node = new JobChainNodeV();
            node.setName(jobChainElem.getAttribute("state"));
            //node.setJobChain(jocXmlCommand.getNestedJobChain(jobChainElem.getAttribute("job_chain"), false));
            node.setState(getNodeState(jobChainElem));
            getNodes().add(node);
        }
    }

    private void setCompactFields() throws Exception {
        setPath();
        setState();
        setSurveyDate(jocXmlCommand.getSurveyDate());
        setName(jobChain.getAttribute(WebserviceConstants.NAME));
        jobNodes = jocXmlCommand.getSosxml().selectNodeList(jobChain, "job_chain_node[@job]");
        jobChainNodes = jocXmlCommand.getSosxml().selectNodeList(jobChain, "job_chain_node[@job_chain] | job_chain_node.job_chain[@job_chain]");
        setNumOfNodes(jobNodes.getLength() + jobChainNodes.getLength());
        //Integer numOfFileOrders = Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(jobChain, "file_order_source/files/@count", "0"));
        Integer numOfBlacklistedOrders = Integer.parseInt(jocXmlCommand.getSosxml().selectSingleNodeValue(jobChain, "blacklist/@count", "0"));
        setNumOfOrders(Integer.parseInt(jobChain.getAttribute("orders")) + numOfBlacklistedOrders);
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
