package com.sos.joc.classes.orders;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class UsedTasks {

    private Map<String, Task> tasks;

    public class Task {

        private JsonArray obstacles;

        public Task(JsonArray obstacles) {
            this.obstacles = obstacles;
        }

        public JsonArray getObstacles() {
            return obstacles;
        }

        public boolean isWaitingForAgent() {
            if (obstacles == null) {
                return false;
            }
            return obstacles.stream().filter(p -> "WaitingForAgent".equals(((JsonObject) p).getString("TYPE", ""))).findFirst().isPresent();
        }
        
        public boolean isWaitingForProcessClass() {
            if (obstacles == null) {
                return false;
            }
            return obstacles.stream().filter(p -> "WaitingForProcessClass".equals(((JsonObject) p).getString("TYPE", ""))).findFirst().isPresent();
        }
    }

    public void addEntries(JsonArray tasks) {
        if (this.tasks == null){
            this.tasks = new HashMap<String, Task>();
            for (JsonObject task : tasks.getValuesAs(JsonObject.class)) {
                this.put(task);
            }
        }
    }
    
    public Task get(String taskId) {
        if (taskId == null) {
            return null;
        }
        return tasks.containsKey(taskId) ? tasks.get(taskId) : null;
    }

    public boolean isWaitingForAgent(String taskId) {
        if (taskId == null) {
            return false;
        }
        return tasks.containsKey(taskId) ? tasks.get(taskId).isWaitingForAgent() : false;
    }
    
    public boolean isWaitingForAgent(Integer taskId) {
        if (taskId == null) {
            return false;
        }
        return isWaitingForAgent(taskId+"");
    }
    
    public boolean isWaitingForProcessClass(String taskId) {
        if (taskId == null) {
            return false;
        }
        return tasks.containsKey(taskId) ? tasks.get(taskId).isWaitingForProcessClass() : false;
    }
    
    public boolean isWaitingForProcessClass(Integer taskId) {
        if (taskId == null) {
            return false;
        }
        return isWaitingForProcessClass(taskId+"");
    }

    private void put(JsonObject task) {
        if (tasks == null) {
            tasks = new HashMap<String, Task>();
        }
        String taskId = task.getString("taskId", null);
        if (taskId != null) {
            tasks.put(taskId, new Task(task.getJsonArray("obstacles")));
        }
    }

}
