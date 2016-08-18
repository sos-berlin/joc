package com.sos.joc.classes;

public class UsedTask {
    Integer taskId;
    String jobPath;
    String state;
    String processClassPath;
    
    public UsedTask() {
        super();
    }
    
    public Integer getTaskId() {
        return taskId;
    }
    public String getTaskIdAsString() {
        return String.valueOf(taskId);
    }
    public void setTaskId(String taskId) {
        try {
            this.taskId = Integer.parseInt(taskId);
        }catch (NumberFormatException e){
            this.taskId = -1;
        }
    }
    public String getJobPath() {
        return jobPath;
    }
    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getProcessClassPath() {
        return processClassPath;
    }
    public void setProcessClassPath(String processClassPath) {
        this.processClassPath = processClassPath;
    }
    
    

}
