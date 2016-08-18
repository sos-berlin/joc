package com.sos.joc.classes;

import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class UsedTasks {
    private HashMap <Integer,UsedTask> usedTasks;
    
    
    public UsedTask get(Integer taskId){
        return usedTasks.get(taskId);
    }
    
    public UsedTask get(String taskId){
        try{
            return this.get(Integer.valueOf(taskId));
        }catch (NumberFormatException e){
            return null;
        }
    }
    
    public void put(UsedTask usedTask){
        if (usedTasks == null){
            usedTasks  = new HashMap<Integer, UsedTask>();
        }
        usedTasks.put(usedTask.getTaskId(),usedTask);
    }
    
    public void addEntries(JsonArray resultUsedTasks){
        for (JsonObject resultUsedTask : resultUsedTasks.getValuesAs(JsonObject.class)) {
            UsedTask usedTask = new UsedTask();
            usedTask.setJobPath(resultUsedTask.getString("jobPath", ""));
            usedTask.setProcessClassPath(resultUsedTask.getString("processClassPath", ""));
            usedTask.setState(resultUsedTask.getString("state", ""));
            usedTask.setTaskId(resultUsedTask.getString("taskId", ""));
            this.put(usedTask);
        }
    }

}
