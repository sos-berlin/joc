package com.sos.joc.classes.jobchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.job.Jobs;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.jobChain.EndNode;
import com.sos.joc.model.jobChain.FileWatchingNodeFile;
import com.sos.joc.model.jobChain.FileWatchingNodeP;
import com.sos.joc.model.jobChain.FileWatchingNodeV;
import com.sos.joc.model.jobChain.JobChainNodeJobP;
import com.sos.joc.model.jobChain.JobChainNodeJobV;
import com.sos.joc.model.jobChain.JobChainNodeP;
import com.sos.joc.model.jobChain.JobChainNodeState;
import com.sos.joc.model.jobChain.JobChainNodeStateText;
import com.sos.joc.model.jobChain.JobChainNodeV;
import com.sos.joc.model.jobChain.JobChainP;
import com.sos.joc.model.jobChain.JobChainState;
import com.sos.joc.model.jobChain.JobChainStateText;
import com.sos.joc.model.jobChain.JobChainV;
import com.sos.joc.model.order.OrdersSummary;

public class JobChains {

    public static List<FileWatchingNodeV> getFileOrderSources() {
        List<FileWatchingNodeV> listOfFileOrderSources = new ArrayList<FileWatchingNodeV>();
        FileWatchingNodeV fileWatchingNodeVSchema = new FileWatchingNodeV();
        fileWatchingNodeVSchema.setDirectory("myDirectory");
        List<FileWatchingNodeFile> listOfFiles = new ArrayList<FileWatchingNodeFile>();
        FileWatchingNodeFile f1 = new FileWatchingNodeFile();
        f1.setModified(new Date());
        f1.setPath("myFile1");
        FileWatchingNodeFile f2 = new FileWatchingNodeFile();
        f2.setModified(new Date());
        f2.setPath("myFile2");
        listOfFiles.add(f1);
        listOfFiles.add(f2);

        fileWatchingNodeVSchema.setFiles(listOfFiles);
        fileWatchingNodeVSchema.setRegex("myRegEx");
        listOfFileOrderSources.add(fileWatchingNodeVSchema);
        return listOfFileOrderSources;
    }
    
    
    private static JobChainV getJobChain(boolean compact){
        JobChainV jobChain = new JobChainV();
//        jobChain.setConfigurationState(ConfigurationStatus.getConfigurationStatus());
//        jobChain.setFileOrderSources(JobChains.getFileOrderSources());
//        jobChain.setName("myName");
        
//        List<Node___> listOfNodes = new ArrayList<Node___>();
//        Node___ node2 = new Node___();
        //node2.setJob(Jobs.getJob(compact));
//        node2.setJob(null);
//        node2.setName("myName");
//        node2.setNumOfOrders(-1);
//        node2.setOrders(Orders.getOrderQueueList());
//        com.sos.joc.model.jobChain.State___ state = new com.sos.joc.model.jobChain.State___();
//        state.setSeverity(-1);
//        state.setText(com.sos.joc.model.jobChain.State___.Text.ACTIVE);
//        node2.setState(state);
        
//        jobChain.setNodes(listOfNodes);
//        jobChain.setNumOfNodes(-1);
//        jobChain.setNumOfOrders(-1);
        jobChain.setPath("myPath");
        
//        State__ stateJobChain = new State__();
//        stateJobChain.setSeverity(-1);
//        stateJobChain.setText(State__.Text.ACTIVE);
////        jobChain.setState(stateJobChain);
//        jobChain.setSurveyDate(new Date());
        return jobChain;

    }
    
    public static JobChainV getJobChain2(boolean compact){
        JobChainV jobChain = new JobChainV();
        jobChain.setName("myName2");
        jobChain.setSurveyDate(new Date());
        jobChain.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
        jobChain.setFileOrderSources(JobChains.getFileOrderSources());

        
        //Nodes
        List<JobChainNodeV> listOfNodes = new ArrayList<JobChainNodeV>();
        JobChainNodeV node = new JobChainNodeV();
        JobChainNodeJobV job = new JobChainNodeJobV();
        job.setPath("myJobPath");
        job.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
        node.setJob(job);
        //node.setJobChain(getJobChain(compact));
        node.setName("myNodeName");
        node.setNumOfOrders(-1);
        node.setOrders(Orders.getOrderQueueList());
        JobChainNodeState state = new JobChainNodeState();
        state.setSeverity(-1);
        state.set_text(JobChainNodeStateText.ACTIVE);
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
        
        JobChainState jobChainState = new JobChainState();
        jobChainState.setSeverity(-1);
        jobChainState.set_text(JobChainStateText.ACTIVE);
        jobChain.setState(jobChainState);
        return jobChain;
    }
    
    public static List<JobChainV> getJobChains(boolean compact){
        List<JobChainV> listOfJobChains = new ArrayList<JobChainV>();
        JobChainV jobChain = getJobChain2(compact);
        listOfJobChains.add(jobChain);
        return listOfJobChains;
    }
    
    public static List<JobChainP> getPJobChains(boolean compact){
        List<JobChainP> listOfJobChains = new ArrayList<JobChainP>();
        JobChainP jobChain = new JobChainP();
        jobChain.setName("myName2");
        jobChain.setSurveyDate(new Date());
        jobChain.setConfigurationDate(new Date());
        jobChain.setDistributed(false);
        List<EndNode> listOfEndNodes = new ArrayList<EndNode>();
        EndNode endNodeSchema = new EndNode();
        endNodeSchema.setMove("myMove");
        endNodeSchema.setName("myName");
        endNodeSchema.setRemove(false);
        listOfEndNodes.add(endNodeSchema);
        jobChain.setEndNodes(listOfEndNodes);
        
        List<FileWatchingNodeP> listOfFileWatchingNodes = new ArrayList<FileWatchingNodeP>();
        FileWatchingNodeP fileWatchingNodePSchema = new FileWatchingNodeP();
        fileWatchingNodePSchema.setDirectory("myDirectory");
        fileWatchingNodePSchema.setNextNode("myNextNode");
        fileWatchingNodePSchema.setRegex("myRegEx");
        listOfFileWatchingNodes.add(fileWatchingNodePSchema);
        jobChain.setFileOrderSources(listOfFileWatchingNodes);
        
        jobChain.setFileWatchingProcessClass("myProcessClass");
        jobChain.setMaxOrders(-1);
        jobChain.setName("myName");
        
        List<JobChainNodeP> listOfNodes = new ArrayList<JobChainNodeP>();
        JobChainNodeP node = new JobChainNodeP();
        node.setDelay(-1);
        node.setErrorNode("myErrorNode");
        JobChainNodeJobP job = new JobChainNodeJobP();
        job.setPath("myJobPath");
        node.setJob(job);
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
