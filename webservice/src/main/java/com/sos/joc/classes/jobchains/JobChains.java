package com.sos.joc.classes.jobchains;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.model.jobChain.EndNode;
import com.sos.joc.model.jobChain.FileWatchingNodeP;
import com.sos.joc.model.jobChain.JobChainNodeJobP;
import com.sos.joc.model.jobChain.JobChainNodeP;
import com.sos.joc.model.jobChain.JobChainP;

public class JobChains {

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
