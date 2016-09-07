package com.sos.joc.classes.jobchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.model.jobChain.EndNodeSchema;
import com.sos.joc.model.jobChain.File;
import com.sos.joc.model.jobChain.FileWatchingNodePSchema;
import com.sos.joc.model.jobChain.FileWatchingNodeVSchema;
import com.sos.joc.model.jobChain.JobChain;
import com.sos.joc.model.jobChain.JobChain__;
import com.sos.joc.model.jobChain.JobChain___;
import com.sos.joc.model.jobChain.Node;
import com.sos.joc.model.jobChain.Node__;
import com.sos.joc.model.jobChain.Node___;
import com.sos.joc.model.jobChain.OrdersSummary;
import com.sos.joc.model.jobChain.State;
import com.sos.joc.model.jobChain.State_;
import com.sos.joc.model.jobChain.State__;

public class JobChains {

    public static List<FileWatchingNodeVSchema> getFileOrderSources() {
        List<FileWatchingNodeVSchema> listOfFileOrderSources = new ArrayList<FileWatchingNodeVSchema>();
        FileWatchingNodeVSchema fileWatchingNodeVSchema = new FileWatchingNodeVSchema();
        fileWatchingNodeVSchema.setDirectory("myDirectory");
        List<File> listOfFiles = new ArrayList<File>();
        File f1 = new File();
        f1.setModified(new Date());
        f1.setPath("myFile1");
        File f2 = new File();
        f2.setModified(new Date());
        f2.setPath("myFile2");
        listOfFiles.add(f1);
        listOfFiles.add(f2);

        fileWatchingNodeVSchema.setFiles(listOfFiles);
        fileWatchingNodeVSchema.setRegex("myRegEx");
        listOfFileOrderSources.add(fileWatchingNodeVSchema);
        return listOfFileOrderSources;
    }
    
    
    private static JobChain___ getJobChain(boolean compact){
        JobChain___ jobChain = new JobChain___();
        jobChain.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
        jobChain.setFileOrderSources(JobChains.getFileOrderSources());
        jobChain.setName("myName");
        
        List<Node___> listOfNodes = new ArrayList<Node___>();
        Node___ node2 = new Node___();
        node2.setJob(Jobs.getJob(compact));
        node2.setName("myName");
        node2.setNumOfOrders(-1);
        node2.setOrders(Orders.getOrderQueueList());
        com.sos.joc.model.jobChain.State___ state = new com.sos.joc.model.jobChain.State___();
        state.setSeverity(-1);
        state.setText(com.sos.joc.model.jobChain.State___.Text.ACTIVE);
        node2.setState(state);
        
        jobChain.setNodes(listOfNodes);
        jobChain.setNumOfNodes(-1);
        jobChain.setNumOfOrders(-1);
        jobChain.setPath("myPath");
        
        State__ stateJobChain = new State__();
        stateJobChain.setSeverity(-1);
        stateJobChain.setText(State__.Text.ACTIVE);
        jobChain.setState(stateJobChain);
        jobChain.setSurveyDate(new Date());
        return jobChain;

    }
    
    public static List<JobChain__> getJobChains(boolean compact){
        List<JobChain__> listOfJobChains = new ArrayList<JobChain__>();
        JobChain__ jobChain = new JobChain__();
        jobChain.setName("myName2");
        jobChain.setSurveyDate(new Date());
        jobChain.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
        jobChain.setFileOrderSources(JobChains.getFileOrderSources());

        
        //Nodes
        List<Node__> listOfNodes = new ArrayList<Node__>();
        Node__ node = new Node__();
        node.setJob(Jobs.getJob(compact));
        node.setJobChain(getJobChain(compact));
        node.setName("myNodeName");
        node.setNumOfOrders(-1);
        node.setOrders(Orders.getOrderQueueList());
        State_ state = new State_();
        state.setSeverity(-1);
        state.setText(State_.Text.ACTIVE);
        node.setState(state);
        listOfNodes.add(node);
        jobChain.setNodes(listOfNodes);
        
        jobChain.setNumOfNodes(-1);
        jobChain.setNumOfOrders(-1);
        OrdersSummary ordersSummary = new OrdersSummary();
        ordersSummary.setBlacklist(-1);
        ordersSummary.setPending(-1);
        ordersSummary.setRunning(-1);
        ordersSummary.setSetback(-1);
        ordersSummary.setSuspended(-1);
        ordersSummary.setWaitingForResource(-1);

        jobChain.setOrdersSummary(ordersSummary);
        jobChain.setPath("myPath");
        
        State jobChainState = new State();
        jobChainState.setSeverity(-1);
        jobChainState.setText(State.Text.ACTIVE);
        jobChain.setState(jobChainState);
        listOfJobChains.add(jobChain);
        return listOfJobChains;
    }
    
    public static List<JobChain> getPJobChains(boolean compact){
        List<JobChain> listOfJobChains = new ArrayList<JobChain>();
        JobChain jobChain = new JobChain();
        jobChain.setName("myName2");
        jobChain.setSurveyDate(new Date());
        jobChain.setConfigurationDate(new Date());
        jobChain.setDistributed(false);
        List<EndNodeSchema> listOfEndNodes = new ArrayList<EndNodeSchema>();
        EndNodeSchema endNodeSchema = new EndNodeSchema();
        endNodeSchema.setMove("myMove");
        endNodeSchema.setName("myName");
        endNodeSchema.setRemove(false);
        listOfEndNodes.add(endNodeSchema);
        jobChain.setEndNodes(listOfEndNodes);
        
        List<FileWatchingNodePSchema> listOfFileWatchingNodes = new ArrayList<FileWatchingNodePSchema>();
        FileWatchingNodePSchema fileWatchingNodePSchema = new FileWatchingNodePSchema();
        fileWatchingNodePSchema.setAlertWhenDirectoryMissing(true);
        fileWatchingNodePSchema.setDelayAfterError(-1);
        fileWatchingNodePSchema.setDirectory("myDirectory");
        fileWatchingNodePSchema.setNextNode("myNextNode");
        fileWatchingNodePSchema.setRegex("myRegEx");
        fileWatchingNodePSchema.setRepeat(-1);
        listOfFileWatchingNodes.add(fileWatchingNodePSchema);
        jobChain.setFileOrderSources(listOfFileWatchingNodes);
        
        jobChain.setFileWatchingProcessClass("myProcessClass");
        jobChain.setMaxOrders(-1);
        jobChain.setName("myName");
        
        List<Node> listOfNodes = new ArrayList<Node>();
        Node node = new Node();
        node.setDelay(-1);
        node.setErrorNode("myErrorNode");
        node.setJob(Jobs.getPJob(compact));
        node.setLevel(-1);
        node.setName("myName");
        node.setNextNode("myNextNode");
        node.setOnError("myOnError");
        listOfNodes.add(node);
        
        jobChain.setNodes(listOfNodes);
        
        jobChain.setNumOfNodes(-1);
        jobChain.setPath("myPath");
        jobChain.setProcessClass("myProcessClass");
        jobChain.setSurveyDate(new Date());
        jobChain.setTitle("myTitle");
    
        listOfJobChains.add(jobChain);
        return listOfJobChains;
    }
}
