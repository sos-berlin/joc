package com.sos.joc.classes.orders;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;


public class UsedNodes {
    
    private Map<String, Node> nodes;
    public class Node {

        private String job;
        private boolean stopped;
        private String nodeId;
        private String jobChain;
        
        public Node(String jobChain, String nodeId) {
        }
        
        public Node(String jobChain, String nodeId, String job, boolean isStopped) {
            this.jobChain = jobChain;
            this.nodeId = nodeId;
            this.job = job;
            this.stopped = isStopped;
        }
        
        public String getJobChain() {
            return jobChain;
        }
        
        public String getNodeId() {
            return nodeId;
        }
        
        public String getJob() {
            return job;
        }
        
        public boolean isStopped() {
            return stopped;
        }
    }
    
//    private Function<JsonValue, String> key = json -> {
//        JsonObject nodeKey = ((JsonObject) json).getJsonObject("nodeKey");
//        return new StringBuilder().append(nodeKey.getString("jobChainPath", "")).append(",").append(nodeKey.getString("nodeId", "")).toString();
//    };
//    
//    private Function<JsonValue, Node> node = json -> {
//        JsonObject jsonO = (JsonObject) json;
//        JsonObject nodeKey = jsonO.getJsonObject("nodeKey");
//        return new Node(nodeKey.getString("jobChainPath", ""),nodeKey.getString("nodeId", ""),jsonO.getString("jobPath", null),"stop".equals(jsonO.getString("action", "")));
//    };
//    
//    public void setUsedNodes(JsonArray nodes) {
//        this.nodes = (Map<String, Node>) nodes.stream().collect( Collectors.toMap(key, node));
//    }
        
    public Node getNode(String jobChain, String nodeId) {
        String s = new StringBuilder().append(jobChain).append(",").append(nodeId).toString();
        if (!nodes.containsKey(s)) {
            throw new IllegalArgumentException(String.format("job chain node '%1$s' in job chain '%2$s' doesn't contain in JobScheduler response.",nodeId,jobChain));
        }
        return nodes.get(s);
    }
    
    public boolean isStopped(String jobChain, String nodeId) {
        return getNode(jobChain, nodeId).isStopped(); 
    }
    
    public String getJob(String jobChain, String nodeId) {
        return getNode(jobChain, nodeId).getJob(); 
    }
    
    public String hasDelay(String jobChain, String nodeId) {
        //TODO getNode(jobChain, nodeId).getObstacles();
        return null;
    }
    
    public void addEntries(JsonArray nodes){
        if (this.nodes == null){
            for (JsonObject node : nodes.getValuesAs(JsonObject.class)) {
                this.put(node);
            }
        } //only fill map if empty
    }
   
    private void put(JsonObject node){
        if (nodes == null){
            nodes  = new HashMap<String, Node>();
        }
        JsonObject nodeKey = node.getJsonObject("nodeKey");
        String nodeId = nodeKey.getString("nodeId", null);
        String jobChain = nodeKey.getString("jobChainPath", null);
        String s = new StringBuilder().append(jobChain).append(",").append(nodeId).toString();
        if(nodeId != null && jobChain != null) {
            Node n = new Node(jobChain,nodeId,node.getString("jobPath", null),"stop".equals(node.getString("action", "")));
            nodes.put(s, n);
        }
    }
}
