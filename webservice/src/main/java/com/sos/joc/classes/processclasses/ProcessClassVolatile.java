package com.sos.joc.classes.processclasses;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.model.processClass.Process;
import com.sos.joc.model.processClass.ProcessClassV;


public class ProcessClassVolatile extends ProcessClassV {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassVolatile.class);
    private final JsonObject processClass;
    private final JsonObject overview;

    public ProcessClassVolatile(JsonObject processClass) {
        this.processClass = processClass;
        this.overview = getOrderOverview();
    }
    
    public void setFields() throws JobSchedulerInvalidResponseDataException {
        setPath();
        setName(getPath().replaceFirst(".*/([^/]+)$", "$1"));
        JsonArray obstacles = overview.getJsonArray("obstacles");
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(obstacles));
        if (processClass.containsKey("processes")) {
            List<Process> listOfProcesses = new ArrayList<Process>();
            JsonArray processes = processClass.getJsonArray("processes");
            setNumOfProcesses(processes.size());
            for (JsonObject processesItem : processes.getValuesAs(JsonObject.class)) {
                Process process = new Process();
                process.setAgent(processesItem.getString("agent", null));
                process.setJob(processesItem.getString("jobPath", null));
                process.setPid(processesItem.getInt("pid", 0));
                process.setRunningSince(JobSchedulerDate.getDateFromISO8601String(processesItem.getString("startedAt", null)));
                process.setTaskId(processesItem.getString("taskId", null));
                listOfProcesses.add(process);
            }
            setProcesses(listOfProcesses);
        } else {
            setNumOfProcesses(0);
        }
        if (getNumOfProcesses() == 0) {
           setProcesses(null); 
        }
    }
    
    public void setPath() throws JobSchedulerInvalidResponseDataException {
        if (getPath() == null) {
            String path = overview.getString("path", null);
            if (path == null) {
                throw new JobSchedulerInvalidResponseDataException("Invalid resonsed data: path is empty");
            } else if (path.isEmpty()) {
                path = "/(default)";
            }
            LOGGER.debug("...processing ProcessClass " + path);
            setPath(path);
        }
    }
    
    public boolean isAgentCluster() {
        return processClass.containsKey("agents") && !processClass.getJsonArray("agents").isEmpty();
    }
    
    private JsonObject getOrderOverview() {
        return processClass.containsKey("overview") ? processClass.getJsonObject("overview") : processClass;
    }
}
