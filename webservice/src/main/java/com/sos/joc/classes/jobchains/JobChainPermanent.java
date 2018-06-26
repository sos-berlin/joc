package com.sos.joc.classes.jobchains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.sos.joc.model.jobChain.JobsFilter;

public class JobChainPermanent {

    public static Set<String> NESTED_JOB_CHAIN_NAMES = new HashSet<String>();
    public static Set<String> JOB_PATHS = new HashSet<String>();

    public static JobChainP initJobChainP(InventoryJobChainsDBLayer dbLayer, DBItemInventoryJobChain inventoryJobChain, Map<Long,String> processClassJobs, Boolean compact,
            Long instanceId, JobsFilter jobsFilter) throws Exception {
        NESTED_JOB_CHAIN_NAMES = new HashSet<String>();
        JOB_PATHS = new HashSet<String>();
        JobChainP jobChain = new JobChainP();
        jobChain.setSurveyDate(inventoryJobChain.getModified());
        jobChain.setPath(inventoryJobChain.getName());
        jobChain.setName(inventoryJobChain.getBaseName());
        List<DBItemInventoryJobChainNode> jobChainNodesFromDb = dbLayer.getJobChainNodesByJobChainId(inventoryJobChain.getId(), instanceId);
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
            Map<String,Integer> levels = new HashMap<String,Integer>();
            if (jobChainNodesFromDb != null) {
                int numOfNodes = 0;
                for (DBItemInventoryJobChainNode node : jobChainNodesFromDb) {
                    switch (node.getNodeType()) {
                    case 1:
                        // JobNode -> Nodes
                    case 2:
                        // JobChainNode -> Nodes
                        numOfNodes += 1;
                        JobChainNodeP jobChainNode = new JobChainNodeP();
                        jobChainNode.setDelay(node.getDelay());
                        jobChainNode.setErrorNode(node.getErrorState());
                        JobChainNodeJobP job = new JobChainNodeJobP();
                        if (node.getJobName() != null && !node.getJobName().isEmpty()) {
                            if (jobsFilter != null) {
                               // 
                            }
                            job.setPath(node.getJobName());
                            if (job.getPath() != null) {
                                JOB_PATHS.add(job.getPath());
                            }
                            job.setProcessClass(processClassJobs.get(node.getJobId()));
                            jobChainNode.setJob(job);
                        } else {
                            jobChainNode.setJob(null);
                        }
                        JobChainNodeJobChainP nodeJobChain = new JobChainNodeJobChainP();
                        if (node.getNestedJobChainName() != null && !DBLayer.DEFAULT_NAME.equalsIgnoreCase(node.getNestedJobChainName())) {
                            nodeJobChain.setPath(node.getNestedJobChainName());
                            jobChainNode.setJobChain(nodeJobChain);
                        } else {
                            jobChainNode.setJobChain(null);
                        }
                        jobChainNode.setName(node.getState());
                        //TODO jobChainNode.setLevel(???);
                        //for now the colons in the node name are counted
                        if (levels.containsKey(jobChainNode.getName())) {
                            jobChainNode.setLevel(levels.get(jobChainNode.getName())); 
                        } else {
                            jobChainNode.setLevel(node.getState().replaceAll("[^:]", "").length());
                        }
                        jobChainNode.setNextNode(node.getNextState());
                        if (!levels.containsKey(jobChainNode.getNextNode())) {
                            levels.put(jobChainNode.getNextNode(), jobChainNode.getLevel()); 
                        }
                        jobChainNode.setOnError(node.getOnError());
                        jobChainNodes.add(jobChainNode);
                        if (node.getNestedJobChainName() != null && !node.getNestedJobChainName().equalsIgnoreCase(DBLayer.DEFAULT_NAME)) {
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
                        numOfNodes += 1;
                        JobChainNodeP fileOrderSink = new JobChainNodeP();
                        JobChainNodeJobP fileOrderSinkJob = new JobChainNodeJobP();
                        fileOrderSinkJob.setPath("/scheduler_file_order_sink");
                        fileOrderSink.setJob(fileOrderSinkJob);
                        fileOrderSink.setJobChain(null);
                        fileOrderSink.setName(node.getState());
                        fileOrderSink.setLevel(0);
                        fileOrderSink.setNextNode("fileOrderSinkEnd");
                        fileOrderSink.setErrorNode("fileOrderSinkEnd");
                        fileOrderSink.setOnError(null);
                        if (node.getFileSinkOp() == 1) {
                            fileOrderSink.setMove(node.getMovePath());
                            fileOrderSink.setRemove(false);
                        } else if (node.getFileSinkOp() == 2) {
                            fileOrderSink.setRemove(true);
                        }
                        jobChainNodes.add(fileOrderSink);
                        
                        EndNode fileOrderSinkEndNode = new EndNode();
                        fileOrderSinkEndNode.setName("fileOrderSinkEnd");
                        if (!jobChainEndNodes.contains(fileOrderSinkEndNode)) {
                            jobChainEndNodes.add(fileOrderSinkEndNode);
                        }
                        
                        break;
                    case 5:
                        // EndNode -> EndNode
                        EndNode endNode = new EndNode();
                        endNode.setName(node.getState());
                        jobChainEndNodes.add(endNode);
                        break;
                    }
                }
                jobChain.setNumOfNodes(numOfNodes);
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
            if (jobChainNodesFromDb != null) {
                int numOfNodes = 0;
                for (DBItemInventoryJobChainNode node : jobChainNodesFromDb) {
                    if (node.getNodeType() < 3) { //JobNode and JobChainNode
                        numOfNodes += 1;
                        if (node.getNodeType() == 1 && node.getJobName() != null && !node.getJobName().isEmpty()) {
                            JOB_PATHS.add(node.getJobName());
                        }
                    }
                }
                jobChain.setNumOfNodes(numOfNodes);
            }
            jobChain.setNodes(null);
            jobChain.setFileOrderSources(null);
            jobChain.setEndNodes(null);
        }
        return jobChain;
    }

}