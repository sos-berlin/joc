package com.sos.joc.classes.orders;

import java.util.ArrayList;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.orders.UsedJobs.Job;
import com.sos.joc.classes.orders.UsedJobChains.JobChain;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.NameValuePairsSchema;
import com.sos.joc.model.job.OrderQueue;
import com.sos.joc.model.job.ProcessingState;


public class OrderV extends OrderQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderV.class);
    private static final String ZERO_HOUR = "1970-01-01T00:00:00Z";
    private JsonObject order;
    private JsonObject overview;
    
    public OrderV(JsonObject order) {
        this.order = order;
        this.overview = getOrderOverview();
    }
    
    public boolean setCompactFields(UsedNodes usedNodes) throws JocException {
        
        JsonObject pState = overview.getJsonObject("processingState");
        JsonArray obstacles = overview.getJsonArray("obstacles");
        LOGGER.info(overview.toString());
        
        //setSurveyDate(null);
        setPathJobChainAndOrderId();
        setState(overview.getString("nodeId", null));
        setHistoryId(getIntField(overview, "historyId"));
        setStartedAt(JobSchedulerDate.getDateFromISO8601String(overview.getString("startedAt", ZERO_HOUR)));
        setProcessedBy(overview.getString("occupyingClusterMemberId", null));
        //setType(OrderQueue.Type.fromValue(unCamelize(overview.getString("sourceType", null))));
        setType(getType(overview.getString("sourceType", "")));
        
        
        setNextStartTime(JobSchedulerDate.getDateFromISO8601String(pState.getString("at", ZERO_HOUR)));
        setSetback(JobSchedulerDate.getDateFromISO8601String(pState.getString("until", ZERO_HOUR)));
        setTaskId(getTaskId(pState.getString("taskId", null)));
        setInProcessSince(JobSchedulerDate.getDateFromISO8601String(pState.getString("since", ZERO_HOUR)));
        setProcessClass(getProcessClass(pState.getString("processClassPath", null)));
        setProcessedBy(pState.getString("agentUri", null));
        //TODO setLock(null);
        
        boolean needMoreInfos = setProcessingState(pState, obstacles, usedNodes);
        ConfigurationStatus.getConfigurationStatus(obstacles);
        setJob(usedNodes.getJob(getJobChain(), getState()));
        setParams(null);
        return needMoreInfos;
    }
    
    public boolean setDetailedFields(UsedNodes usedNodes) throws JocException {
        
        boolean needMoreInfos = setCompactFields(usedNodes);
        setStateText(order.getString("stateText", null));
        setPriority(getIntField(order,"priority"));
        setEndState(order.getString("endNodeId", null));
        setParamsFromJson(order.getJsonObject("variables"));
        return needMoreInfos;
    }
    
    public boolean setProcessingState(JsonObject processingState, JsonArray obstacles, UsedNodes usedNodes) {
        boolean needMoreInfos = false;
        switch(processingState.getString("TYPE","")) {
            case "NotPlanned": 
                setSeverity(ProcessingState.Text.PENDING);
                break;
            case "Planned":
                setSeverity(ProcessingState.Text.PENDING);
                break;
            case "Setback":
                setSeverity(ProcessingState.Text.SETBACK);
                break;
            case "InTaskProcess":
                setSeverity(ProcessingState.Text.RUNNING);
            case "WaitingInTask":
                setSeverity(ProcessingState.Text.WAITING_FOR_AGENT);
                break;
            case "Pending":
            case "WaitingForOther":
                for (JsonObject obstacle : obstacles.getValuesAs(JsonObject.class)) {
                    if ("Suspended".equals(obstacle.getString("TYPE", null))) {
                        setSeverity(ProcessingState.Text.SUSPENDED); 
                        break;
                    }
                }
                if (getProcessingState() == null && usedNodes.getNode(getJobChain(), getState()).isStopped()) {
                    setSeverity(ProcessingState.Text.NODE_STOPPED);
                }
                if (getProcessingState() == null) {
                    needMoreInfos = true;
                }
                break;
            case "Blacklisted":
                setSeverity(ProcessingState.Text.BLACKLIST);
                break;
            default:
        }
        return needMoreInfos;
    }
    
    public boolean hasJobObstacles(Job job) {
        ProcessingState.Text text = job.getState();
        if (text == ProcessingState.Text.JOB_NOT_IN_PERIOD) {
            setNextStartTime(JobSchedulerDate.getDateFromISO8601String(job.nextPeriodBeginsAt()));
            return false;
        }
        if (text == ProcessingState.Text.JOB_STOPPED) {
            return false;
        }
        if (text == ProcessingState.Text.WAITING_FOR_TASK) {
            return false;
        }
        if (text == ProcessingState.Text.WAITING_FOR_PROCESS) {
            return false;
        }
        if (text == ProcessingState.Text.WAITING_FOR_LOCK) {
            setLock(job.getLock());
            return false;
        }
        return true;
    }
    
    public boolean hasJobChainObstacles(JobChain jobChain) {
        if (jobChain.isStopped()) {
            setSeverity(ProcessingState.Text.JOB_CHAIN_STOPPED);
            return false;
        }
        return true;
    }
    
    private void setSeverity(ProcessingState.Text text) {
        setProcessingState(new ProcessingState());
        getProcessingState().setText(text);
        switch (text) {
        case PENDING:
            getProcessingState().setSeverity(1);
            break;
        case RUNNING:
            getProcessingState().setSeverity(0);
            break;
        case SETBACK:
        case SUSPENDED:
            getProcessingState().setSeverity(5);
            break;
        case WAITING_FOR_AGENT:
        case JOB_CHAIN_STOPPED:
        case JOB_STOPPED:
        case NODE_STOPPED:
            getProcessingState().setSeverity(2);
            break;
        default:
            getProcessingState().setSeverity(3);
            break;
        }
    }
    
    private void setPathJobChainAndOrderId() throws JocException {
        String path = overview.getString("path", null);
        if (path == null || path.isEmpty()) {
            throw new JobSchedulerInvalidResponseDataException("Invalid resonsed data: path is empty");
        }
        LOGGER.debug("...processing Order " + path);
        String[] pathParts = path.split(",", 2);
        if (pathParts.length != 2) {
            throw new JobSchedulerInvalidResponseDataException(String.format("Invalid resonsed data: path ('%1$s') doesn't contain the orderId",path));
        }
        setPath(path);
        setJobChain(pathParts[0]);
        setOrderId(pathParts[1]);
    }
    
    private Integer getTaskId(String taskId) {
        if (taskId != null) {
            try {
                return Integer.valueOf(taskId);
            } catch (NullPointerException e) {
                //
            } catch (NumberFormatException e) {
                LOGGER.info(String.format("Invalid resonsed data: taskId='%1$s'", taskId));
            }
        }
        return null;
    }
    
    private String getProcessClass(String processClass) {
        if(processClass == null || processClass.isEmpty()) {
            //default process class is empty and should not be set
            processClass = null;
        }
        return processClass;
    }
    
    private Integer getIntField(JsonObject json, String fieldName) {
        JsonNumber num = json.getJsonNumber(fieldName);
        if (num != null) {
            try {
                return num.intValue();
            } catch (Exception e) {
                LOGGER.info(String.format("Invalid resonsed data: %1$s='%2$s'",fieldName, num.toString()));
            }
        }
        return null;
    }
    
    private void setParamsFromJson(JsonObject variables) {
        if (variables != null) {
            if (getParams() == null) {
                setParams(new ArrayList<NameValuePairsSchema>());
            }
            for (String key : variables.keySet()) {
                NameValuePairsSchema param = new NameValuePairsSchema();
                param.setName(key);
                param.setValue(variables.getString(key, ""));
                getParams().add(param);
            }
        } else {
            setParams(null); 
        }
    }
    
    private JsonObject getOrderOverview() {
        return order.containsKey("overview") ? order.getJsonObject("overview") : order;
    }
    
//    private String unCamelize(String s) {
//        if (s != null) {
//            s = s.trim().replaceAll("(?<!_)([A-Z])", "_$1").replaceFirst("^_", "").toUpperCase();
//        }
//        return s;
//    }
    
    private Type getType(String s) {
        switch(s) {
        case "AdHoc":
            return Type.AD_HOC;
        case "FileOrder":
            return Type.FILE_ORDER;
        case "Permanent":
            return Type.PERMANENT;
        default:
            return Type.PERMANENT;
        }
    }

}
