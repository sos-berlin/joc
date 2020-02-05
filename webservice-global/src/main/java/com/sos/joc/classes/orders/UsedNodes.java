package com.sos.joc.classes.orders;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;


public class UsedNodes {
    
    private Map<String, Node> nodes;
    public enum State {
        STOPPED,
        WAITINGFORJOB,
        DELAYED,
        NOOBSTACLE
    }
    public class Node {

        private String job;
        private String nodeId;
        private String jobChain;
        private JsonArray obstacles;
        private boolean parsed = false;
        private State state = State.NOOBSTACLE;
        
        public Node(String jobChain, String nodeId, String job, JsonArray obstacles) {
            this.jobChain = jobChain;
            this.nodeId = nodeId;
            this.job = job;
            this.obstacles = obstacles;
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
        
        public State getState() {
            parse();
            return state;
        }
        
        public boolean isStopped() {
            return getState() == State.STOPPED;
        }
        
        public boolean isWaitingForJob() {
            return getState() == State.WAITINGFORJOB;
        }
        
        public boolean hasNoObstacle() {
            return getState() == State.NOOBSTACLE;
        }
        
        private void parse() {
            if (!parsed && obstacles != null) {
                parsed = true;
                for (JsonObject obstacle : obstacles.getValuesAs(JsonObject.class)) {
                    switch(obstacle.getString("TYPE","")) {
                    case "WaitingForJob":
                        state = State.WAITINGFORJOB;
                        break;
                    case "Stopping":
                        state = State.STOPPED;
                        break;
//                    case "Delaying":
//                        duration = obstacle.getInt("duration");
//                        break;
                    }
                }
            }
        }
    }
    
//    private Function<JsonValue, String> key = json -> {
//        JsonObject jsonO = (JsonObject) json;
//        return new StringBuilder().append(jsonO.getString("jobChainPath", "")).append(",").append(jsonO.getString("nodeId", "")).toString();
//    };
//    
//    private Function<JsonValue, Node> node = json -> {
//        JsonObject jsonO = (JsonObject) json;
//        return new Node(jsonO.getString("jobChainPath", ""),jsonO.getString("nodeId", ""),jsonO.getString("jobPath", null),"stop".equals(jsonO.getString("action", "")));
//    };
//    
//    public void addEntries(JsonArray nodes) {
//        if (this.nodes == null) {
//            this.nodes = new HashMap<String, Node>();
//            this.nodes = (Map<String, Node>) nodes.stream().collect( Collectors.toMap(key, node));
//        }
//    }
        
    public Node getNode(String jobChain, String nodeId) {
        String s = new StringBuilder().append(jobChain).append(",").append(nodeId).toString();
        if (!nodes.containsKey(s)) {
            return null;
        }
        return nodes.get(s);
    }
    
    public boolean isStopped(String jobChain, String nodeId) {
        Node n = getNode(jobChain, nodeId);
        if (n == null) {
            return false;
        }
        return n.isStopped(); 
    }
    
    public boolean isWaitingForJobs(String jobChain, String nodeId) {
        Node n = getNode(jobChain, nodeId);
        if (n == null) {
            return false;
        }
        return n.isWaitingForJob(); 
    }
    
    public boolean hasNoObstacle(String jobChain, String nodeId) {
        Node n = getNode(jobChain, nodeId);
        if (n == null) {
            return true;
        }
        return n.hasNoObstacle(); 
    }
    
    public String getJob(String jobChain, String nodeId) {
        Node n = getNode(jobChain, nodeId);
        if (n == null) {
            return null;
        }
        return n.getJob(); 
    }
    
    public void addEntries(JsonArray nodes){
        if (this.nodes == null) {
            this.nodes = new HashMap<String, Node>();
            for (JsonObject node : nodes.getValuesAs(JsonObject.class)) {
                this.put(node);
            }
        } //only fill map if empty
    }
   
    private void put(JsonObject node){
        String nodeId = node.getString("nodeId", null);
        String jobChain = node.getString("jobChainPath", null);
        if(nodeId != null && jobChain != null) {
            String s = new StringBuilder().append(jobChain).append(",").append(nodeId).toString();
            Node n = new Node(jobChain,nodeId,node.getString("jobPath", null),node.getJsonArray("obstacles"));
            nodes.put(s, n);
        }
    }
}
