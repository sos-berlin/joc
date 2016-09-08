package com.sos.joc.classes.orders;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.model.job.ProcessingState;


public class UsedJobs {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UsedJobs.class);
    private HashMap <String,Job> jobs;
    
    private class Job {

        private JsonArray obstacles;
        private String nextPeriodBeginsAt = null;
        private String lock = null;
        private Integer taskLimit = null;
        private Integer processLimit = null;
        private boolean stopped = false;
        private boolean parsed = false;
        private ProcessingState.Text state;

        public Job(JsonArray obstacles) {
            this.obstacles = obstacles;
        }

        public void parse() {
            if (!parsed && obstacles != null) {
                parsed = true;
                for (JsonObject obstacle : obstacles.getValuesAs(JsonObject.class)) {
                    switch(obstacle.getString("TYPE","")) {
                    case "NoRuntime":
                        nextPeriodBeginsAt = obstacle.getString("plannedAt");
                        state = ProcessingState.Text.JOB_NOT_IN_PERIOD;
                        break;
                    case "Stopped":
                        stopped = true;
                        state = ProcessingState.Text.JOB_STOPPED;
                        break;
                    case "TaskLimitReached":
                        taskLimit = obstacle.getInt("limit");
                        state = ProcessingState.Text.WAITING_FOR_TASK;
                        break;
                    case "ProcessLimitReached":
                        processLimit = obstacle.getInt("limit");
                        state = ProcessingState.Text.WAITING_FOR_PROCESS;
                        break;
                    case "Locked":
                        lock = obstacle.getString("path");
                        state = ProcessingState.Text.WAITING_FOR_LOCK;
                        break;
                    }
                }
            }
        }
        
        public boolean isStopped() {
            parse();
            return stopped;
        }
        
        public String nextPeriodBeginsAt() {
            parse();
            return nextPeriodBeginsAt;
        }
        
        public Integer getTaskLimit() {
            parse();
            return taskLimit;
        }
        
        public Integer getProcessLimit() {
            parse();
            return processLimit;
        }
        
        public String getLock() {
            parse();
            return lock;
        }
        
        public ProcessingState.Text getState() {
            parse();
            return state;
        }
    }
    
    public void addEntries(JsonArray jobs){
        for (JsonObject job : jobs.getValuesAs(JsonObject.class)) {
            this.put(job);
        }
    }
    
    public void parse(String path) {
        if (jobs.containsKey(path)) {
            jobs.get(path).parse();
        }
    }
    
    public boolean isStopped(String path) {
        return jobs.containsKey(path) ? jobs.get(path).isStopped() : false;
    }
    
    public Integer getTaskLimit(String path) {
        return jobs.containsKey(path) ? jobs.get(path).getTaskLimit() : null;
    }
    
    public Integer getProcessLimit(String path) {
        return jobs.containsKey(path) ? jobs.get(path).getProcessLimit() : null;
    }
    
    public String getLock(String path) {
        return jobs.containsKey(path) ? jobs.get(path).getLock() : null;
    }
    
    public ProcessingState.Text getState(String path) {
        return jobs.containsKey(path) ? jobs.get(path).getState() : null;
    }
    
    /**
     * 
     * @param path
     * @return
     *   null if job is in period
     */
    public Date nextPeriodBeginsAt(String path) {
        if (jobs.containsKey(path)) {
            String nextPeriodBeginsAt = jobs.get(path).nextPeriodBeginsAt();
            try {
                return Date.from(Instant.parse(nextPeriodBeginsAt));
            }
            catch (DateTimeParseException e) {
                String msg = String.format("Job '%1$s': Next period begin '%2$s' has invalid datetime format", path, nextPeriodBeginsAt);
                LOGGER.error(msg,e);
            }
        }
        return null; 
    }

    private void put(JsonObject job) {
        if (jobs == null){
            jobs  = new HashMap<String, Job>();
        }
        String path = job.getString("path");
        if(path != null) {
            jobs.put(path, new Job(job.getJsonArray("obstacles")));
        }
    }
}
