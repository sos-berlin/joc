package com.sos.joc.classes.orders;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class UsedJobChains {

    private Map<String, JobChain> jobChains;

    public class JobChain {

        private JsonArray obstacles;

        public JobChain(JsonArray obstacles) {
            this.obstacles = obstacles;
        }

        public JsonArray getObstacles() {
            return obstacles;
        }

        public boolean isStopped() {
            if (obstacles == null) {
                return false;
            }
            return obstacles.stream().filter(p -> "Stopped".equals(((JsonObject) p).getString("TYPE", ""))).findFirst().isPresent();
        }
    }

    public void addEntries(JsonArray jobChains) {
        if (this.jobChains == null){
            this.jobChains = new HashMap<String, JobChain>();
            for (JsonObject jobChain : jobChains.getValuesAs(JsonObject.class)) {
                this.put(jobChain);
            }
        }
    }
    
    public JobChain get(String path) {
        return jobChains.containsKey(path) ? jobChains.get(path) : null;
    }

    public boolean isStopped(String path) {
        return jobChains.containsKey(path) ? jobChains.get(path).isStopped() : false;
    }

    private void put(JsonObject jobChain) {
        if (jobChains == null) {
            jobChains = new HashMap<String, JobChain>();
        }
        String path = jobChain.getString("path", null);
        if (path != null) {
            jobChains.put(path, new JobChain(jobChain.getJsonArray("obstacles")));
        }
    }

}
