package com.sos.joc.classes.orders;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.model.job.ProcessingState;


public class UsedJobs {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UsedJobs.class);
    private HashMap <String,Job> jobs;
    
    public class Job {

        private JsonArray obstacles;
        private String nextPeriodBeginsAt = null;
        private JsonArray locks = null;
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
                    case "WaitingForLocks":
                        locks = obstacle.getJsonArray("lockPaths");
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
        
        public JsonArray getLocks() {
            parse();
            return locks;
        }
        
        public String getLock() {
            JsonArray l = getLocks();
            if (l != null) {
                return l.get(0).toString();
            }
            return null;
        }
        
        public ProcessingState.Text getState() {
            parse();
            return state;
        }
    }
    
    public void addEntries(JsonArray jobs){
        if (this.jobs == null){
            for (JsonObject job : jobs.getValuesAs(JsonObject.class)) {
                this.put(job);
            }
        } 
    }
    
    public void parse(String path) {
        if (jobs.containsKey(path)) {
            jobs.get(path).parse();
        }
    }
    
    public Job get(String path) {
        if (jobs.containsKey(path)) {
            return jobs.get(path);
        }
        return null;
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
    
    public List<String> getLocks(String path) {
        JsonArray jlocks = jobs.containsKey(path) ? jobs.get(path).getLocks() : null;
        if (jlocks != null) {
            List<String> locks = new ArrayList<String>();
            for (JsonString lock : jlocks.getValuesAs(JsonString.class)) {
                locks.add(lock.getString());
            }
            return locks;
        }
        return null;
    }
    
    public String getLock(String path) {
        JsonArray locks = jobs.containsKey(path) ? jobs.get(path).getLocks() : null;
        if (locks != null) {
            return locks.get(0).toString();
        }
        return null;
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
