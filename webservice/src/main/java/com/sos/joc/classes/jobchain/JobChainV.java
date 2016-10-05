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
import com.sos.joc.classes.jobs.JobV;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.jobChain.File;
import com.sos.joc.model.jobChain.FileWatchingNodeVSchema;
import com.sos.joc.model.jobChain.JobChain__;
import com.sos.joc.model.jobChain.Node__;
import com.sos.joc.model.jobChain.State;
import com.sos.joc.model.jobChain.State_;
import com.sos.joc.model.jobChain.State_.Text;


public class JobChainV extends JobChain__ {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainV.class);
    private Element jobChain;
    private JOCXmlJobChainCommand jocXmlCommand;
    private NodeList jobNodes = null;
    private NodeList jobChainNodes = null;

    public JobChainV() {
        super();
    }
    
    public JobChainV(Element jobChain, JOCXmlJobChainCommand jocXmlCommand) {
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
            setState(new State());
            try {
                getState().setText(State.Text.fromValue(jobChain.getAttribute(WebserviceConstants.STATE).toUpperCase()));
            } catch (Exception e) {
                getState().setText(State.Text.ACTIVE);
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
    
    public void setOrders(List<OrderQueue> orders) {
        Map<String,List<OrderQueue>> nodeMap = new HashMap<String,List<OrderQueue>>();
        for (OrderQueue order : orders) {
            String node = order.getState();
            if (!nodeMap.containsKey(node)) {
                nodeMap.put(node, new ArrayList<OrderQueue>()); 
            }
            nodeMap.get(node).add(order);
        }
        for (Node__ node : getNodes()) {
            List<OrderQueue> o = nodeMap.get(node.getName());
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
        setNodes(new ArrayList<Node__>());
        for (int i=0; i < jobNodes.getLength(); i++) {
            Element jobNodeElem = (Element) jobNodes.item(i);
            Node__ node = new Node__();
            node.setName(jobNodeElem.getAttribute("state"));
            JobV job = new JobV((Element) jocXmlCommand.getSosxml().selectSingleNode(jobNodeElem, "job"), jocXmlCommand);
            job.setPath(jobNodeElem.getAttribute(WebserviceConstants.PATH));
            job.setState();
            job.setLocks(null);
            job.setOrderQueue(null);
            job.setParams(null);
            job.setRunningTasks(null);
            job.setTaskQueue(null);
            node.setJob(job);
            node.setState(getNodeState(jobNodeElem));
            getNodes().add(node);
        }
        for (int i=0; i < jobChainNodes.getLength(); i++) {
            Element jobChainElem = (Element) jobChainNodes.item(i);
            Node__ node = new Node__();
            node.setName(jobChainElem.getAttribute("state"));
            node.setJobChain(jocXmlCommand.getNestedJobChain(jobChainElem.getAttribute("job_chain"), false));
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
    
    private List<FileWatchingNodeVSchema> getFileWatchingNodeVSchema() throws Exception {
        NodeList fileOrderSources = jocXmlCommand.getSosxml().selectNodeList(jobChain, "file_order_source");
        if (fileOrderSources.getLength() == 0) {
            return null;
        }
        List<FileWatchingNodeVSchema> fileWatchingNodes = new ArrayList<FileWatchingNodeVSchema>();
        for (int i=0; i < fileOrderSources.getLength(); i++) {
           Element fileOrderSourceElement = (Element) fileOrderSources.item(i);
           FileWatchingNodeVSchema fileOrderSource = new FileWatchingNodeVSchema();
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
               List<File> files = new ArrayList<File>();
               for (int j=0; i < fileOrders.getLength(); j++) {
                   Element fileOrder = (Element) fileOrders.item(j);
                   File file = new File();
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
    
    private void setSeverity(State state) {
        switch (state.getText()) {
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
    
    private State_ getNodeState(Element elem) {
        State_ state = new State_();
        switch(elem.getAttribute("action")) {
        case "stop":
            state.setSeverity(2);
            state.setText(Text.STOPPED);
            return state;
        case "next_state":
            state.setSeverity(5);
            state.setText(Text.SKIPPED);
            return state;
        default:
            state.setSeverity(4);
            state.setText(Text.ACTIVE);
            return state;
        }
    }
}
