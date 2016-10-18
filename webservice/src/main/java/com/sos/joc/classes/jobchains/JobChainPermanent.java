package com.sos.joc.classes.jobchains;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryJobChainNode;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.model.jobChain.EndNode;
import com.sos.joc.model.jobChain.FileWatchingNodeP;
import com.sos.joc.model.jobChain.JobChainNodeJobChainP;
import com.sos.joc.model.jobChain.JobChainNodeJobP;
import com.sos.joc.model.jobChain.JobChainNodeP;
import com.sos.joc.model.jobChain.JobChainP;

public class JobChainPermanent {

    public static final Set<String> NESTED_JOB_CHAIN_NAMES = new HashSet<String>();

//    public static List<JobChainP> getPJobChains(boolean compact){
//        List<JobChainP> listOfJobChains = new ArrayList<JobChainP>();
//        JobChainP jobChain = new JobChainP();
//        jobChain.setName("myName2");
//        jobChain.setSurveyDate(new Date());
//        jobChain.setConfigurationDate(new Date());
//        jobChain.setDistributed(false);
//        List<EndNode> listOfEndNodes = new ArrayList<EndNode>();
//        EndNode endNodeSchema = new EndNode();
//        endNodeSchema.setMove("myMove");
//        endNodeSchema.setName("myName");
//        endNodeSchema.setRemove(false);
//        listOfEndNodes.add(endNodeSchema);
//        jobChain.setEndNodes(listOfEndNodes);
//        
//        List<FileWatchingNodeP> listOfFileWatchingNodes = new ArrayList<FileWatchingNodeP>();
//        FileWatchingNodeP fileWatchingNodePSchema = new FileWatchingNodeP();
//        fileWatchingNodePSchema.setDirectory("myDirectory");
//        fileWatchingNodePSchema.setNextNode("myNextNode");
//        fileWatchingNodePSchema.setRegex("myRegEx");
//        listOfFileWatchingNodes.add(fileWatchingNodePSchema);
//        jobChain.setFileOrderSources(listOfFileWatchingNodes);
//        
//        jobChain.setFileWatchingProcessClass("myProcessClass");
//        jobChain.setMaxOrders(-1);
//        jobChain.setName("myName");
//        
//        List<JobChainNodeP> listOfNodes = new ArrayList<JobChainNodeP>();
//        JobChainNodeP node = new JobChainNodeP();
//        node.setDelay(-1);
//        node.setErrorNode("myErrorNode");
//        JobChainNodeJobP job = new JobChainNodeJobP();
//        job.setPath("myJobPath");
//        node.setJob(job);
//        node.setLevel(-1);
//        node.setName("myName");
//        node.setNextNode("myNextNode");
//        node.setOnError("myOnError");
//        listOfNodes.add(node);
//        
//        jobChain.setNodes(listOfNodes);
//        
//        jobChain.setNumOfNodes(-1);
//        jobChain.setPath("myPath");
//        jobChain.setProcessClass("myProcessClass");
//        jobChain.setSurveyDate(new Date());
//        jobChain.setTitle("myTitle");
//    
//        listOfJobChains.add(jobChain);
//        return listOfJobChains;
//    }
//
    public static JobChainP initJobChainP(InventoryJobChainsDBLayer dbLayer, DBItemInventoryJobChain inventoryJobChain, Boolean compact) throws Exception {
        JobChainP jobChain = new JobChainP();
        jobChain.setSurveyDate(inventoryJobChain.getModified());
        jobChain.setPath(inventoryJobChain.getName());
        jobChain.setName(inventoryJobChain.getBaseName());
        List<DBItemInventoryJobChainNode> jobChainNodesFromDb = dbLayer.getJobChainNodesByJobChainId(inventoryJobChain.getId());
        if (jobChainNodesFromDb != null) {
            jobChain.setNumOfNodes(jobChainNodesFromDb.size());
        }
        jobChain.setTitle(inventoryJobChain.getTitle());
        jobChain.setMaxOrders(inventoryJobChain.getMaxOrders());
        jobChain.setDistributed(inventoryJobChain.getDistributed());
        if (!inventoryJobChain.getProcessClassName().equalsIgnoreCase(DBLayer.DEFAULT_NAME)) {
            jobChain.setProcessClass(inventoryJobChain.getProcessClassName());
        } else if (inventoryJobChain.getProcessClass() != null){
            jobChain.setProcessClass(inventoryJobChain.getProcessClass());
        }
        if (!inventoryJobChain.getFileWatchingProcessClassName().equalsIgnoreCase(DBLayer.DEFAULT_NAME)) {
            jobChain.setFileWatchingProcessClass(inventoryJobChain.getFileWatchingProcessClassName());
        } else if (inventoryJobChain.getFileWatchingProcessClass() != null) {
            jobChain.setFileWatchingProcessClass(inventoryJobChain.getFileWatchingProcessClass());
        }
        jobChain.setFileWatchingProcessClass(inventoryJobChain.getFileWatchingProcessClass());
        if (compact != null && !compact) {
            jobChain.setConfigurationDate(dbLayer.getJobChainConfigurationDate(inventoryJobChain.getId()));
            List<JobChainNodeP> jobChainNodes = new ArrayList<JobChainNodeP>();
            List<EndNode> jobChainEndNodes = new ArrayList<EndNode>();
            List<FileWatchingNodeP> jobChainFileOrderSources = new ArrayList<FileWatchingNodeP>();
            for (DBItemInventoryJobChainNode node : jobChainNodesFromDb) {
                switch(node.getNodeType()) {
                case 1:
                    // JobNode -> Nodes
                case 2:
                    // JobChainNode -> Nodes
                    JobChainNodeP jobChainNode = new JobChainNodeP();
                    jobChainNode.setDelay(node.getDelay());
                    jobChainNode.setErrorNode(node.getErrorState());
                    JobChainNodeJobP job = new JobChainNodeJobP();
                    if(node.getJob() != null && !"".equalsIgnoreCase(node.getJob())) {
                        job.setPath(node.getJobName());
                        jobChainNode.setJob(job);
                    } else {
                        jobChainNode.setJob(null);
                    }
                    JobChainNodeJobChainP nodeJobChain = new JobChainNodeJobChainP();
                    if(node.getNestedJobChainName() != null && !DBLayer.DEFAULT_NAME.equalsIgnoreCase(node.getNestedJobChainName())) {
                        nodeJobChain.setPath(node.getNestedJobChainName());
                        jobChainNode.setJobChain(nodeJobChain);
                    } else {
                        jobChainNode.setJobChain(null);
                    }
//                    jobChainNode.setLevel(???);
                    jobChainNode.setName(node.getState());
                    jobChainNode.setNextNode(node.getNextState());
                    jobChainNode.setOnError(node.getOnError());
                    jobChainNodes.add(jobChainNode);
                    if(node.getNestedJobChainName() != null && !node.getNestedJobChainName().equalsIgnoreCase(DBLayer.DEFAULT_NAME)) {
                        NESTED_JOB_CHAIN_NAMES.add(node.getNestedJobChainName());
                    } else if (node.getNestedJobChain() != null) {
                        NESTED_JOB_CHAIN_NAMES.add(node.getNestedJobChain());
                    }
                    break;
                case 3:
                    // FileOrderSource -> FileOrderSource
                    FileWatchingNodeP fileOrderSource = new FileWatchingNodeP();
                    fileOrderSource.setDirectory(node.getDirectory());
                    fileOrderSource.setNextNode(node.getNextState());
                    fileOrderSource.setRegex(node.getRegex());
                    jobChainFileOrderSources.add(fileOrderSource);
                    break;
                case 4:
                    // FileOrderSink -> EndNode
                    EndNode fileOrderSink = new EndNode();
                    fileOrderSink.setName(node.getState());
                    if (node.getFileSinkOp() == 1) {
                        fileOrderSink.setMove(node.getMovePath());
                        fileOrderSink.setRemove(false);
                    } else if (node.getFileSinkOp() == 2) {
                        fileOrderSink.setRemove(true);
                    }
                    jobChainEndNodes.add(fileOrderSink);
                    break;
                case 5:
                    // EndNode -> EndNode
                    EndNode endNode = new EndNode();
                    endNode.setName(node.getState());
                    jobChainEndNodes.add(endNode);
                    break;
                }
            }                
            if (!jobChainNodes.isEmpty()) {
                jobChain.setNodes(jobChainNodes);
            } else {
                jobChain.setNodes(null);
            }
            if (!jobChainFileOrderSources.isEmpty()) {
                jobChain.setFileOrderSources(jobChainFileOrderSources);
            } else {
                jobChain.setFileOrderSources(null);
            }
            if (!jobChainEndNodes.isEmpty()) {
                jobChain.setEndNodes(jobChainEndNodes);
            } else {
                jobChain.setEndNodes(null);
            }
        } else {
            jobChain.setNodes(null);
            jobChain.setFileOrderSources(null);
            jobChain.setEndNodes(null);
        }
        return jobChain;
    }

}