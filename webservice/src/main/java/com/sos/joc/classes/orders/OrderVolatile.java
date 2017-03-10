package com.sos.joc.classes.orders;

import java.time.Instant;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.orders.UsedJobs.Job;
import com.sos.joc.classes.orders.UsedTasks.Task;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.classes.orders.UsedJobChains.JobChain;
import com.sos.joc.exceptions.JobSchedulerInvalidResponseDataException;
import com.sos.joc.model.order.OrderState;
import com.sos.joc.model.order.OrderStateFilter;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderType;
import com.sos.joc.model.order.OrderV;


public class OrderVolatile extends OrderV {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderVolatile.class);
    //private static final String ZERO_HOUR = "1970-01-01T00:00:00Z";
    private final JsonObject order;
    private final JsonObject overview;
    @JsonIgnore
    private boolean isWaitingForJob = false;
    @JsonIgnore
    private String origJobChain;
    @JsonIgnore
    private OrderStateFilter processingFilterState;
    
    public OrderVolatile(JsonObject order) {
        this.order = order;
        this.overview = getOrderOverview();
        this.origJobChain = null;
    }
    
    public OrderVolatile(JsonObject order, String origJobChain) {
        this.order = order;
        this.overview = getOrderOverview();
        this.origJobChain = origJobChain;
    }
    
    @JsonIgnore
    public boolean isWaitingForJob() {
        return isWaitingForJob;
    }
    
    @JsonIgnore
    public String origJobChain() {
        if (origJobChain == null) {
            return getJobChain();
        }
        return origJobChain;
    }

    public void setFields(UsedNodes usedNodes, UsedTasks usedTasks, boolean compact) throws JobSchedulerInvalidResponseDataException {
        if (compact) {
            setCompactFields(usedNodes, usedTasks);
            cleanArrays();
        } else {
            setDetailedFields(usedNodes, usedTasks);
        }
    }
    
    public void setCompactFields(UsedNodes usedNodes, UsedTasks usedTasks) throws JobSchedulerInvalidResponseDataException {
        
        JsonObject pState = overview.getJsonObject("orderProcessingState");
        JsonArray obstacles = overview.getJsonArray("obstacles");
        LOGGER.debug(overview.toString());
        
        //setSurveyDate(null);
        //setPathJobChainAndOrderId();
        setState(overview.getString("nodeId", null));
        setHistoryId(getStringFromIntField(overview, "historyId"));
        setStartedAt(JobSchedulerDate.getDateFromISO8601String(overview.getString("startedAt", Instant.EPOCH.toString())));
        if (overview.containsKey("orderSourceType")) {
            set_type(getType(overview.getString("orderSourceType", ""))); 
        } else {
            set_type(getType(overview.getString("sourceType", "")));
        }
        setNextStartTime(JobSchedulerDate.getDateFromISO8601String(pState.getString("at", Instant.EPOCH.toString())));
        setSetback(JobSchedulerDate.getDateFromISO8601String(pState.getString("until", Instant.EPOCH.toString())));
        setTaskId(pState.getString("taskId", null));
        setInProcessSince(JobSchedulerDate.getDateFromISO8601String(pState.getString("since", Instant.EPOCH.toString())));
        setProcessClass(getProcessClass(pState.getString("processClassPath", null)));
        String agentUri = pState.getString("agentUri", null);
        if (agentUri != null && !agentUri.isEmpty()) {
            setProcessedBy(agentUri);
        } else {
            setProcessedBy(pState.getString("clusterMemberId", null));
        }
        setProcessingState(pState, obstacles, usedNodes, usedTasks);
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(obstacles));
        setJob(usedNodes.getJob(getJobChain(), getState()));
    }
    
    public void setDetailedFields(UsedNodes usedNodes, UsedTasks usedTasks) throws JobSchedulerInvalidResponseDataException {
        
        setCompactFields(usedNodes, usedTasks);
        setStateText(order.getString("stateText", null));
        setPriority(getIntField(order,"priority"));
        setEndState(order.getString("endNodeId", null));
        setParams(Parameters.getParameters(order));
    }
    
    public void setProcessingState(JsonObject processingState, JsonArray obstacles, UsedNodes usedNodes, UsedTasks usedTasks) {
        for (JsonObject obstacle : obstacles.getValuesAs(JsonObject.class)) {
            if ("Suspended".equals(obstacle.getString("TYPE", null))) {
                setSeverity(OrderStateText.SUSPENDED);
            }
        }
        if (!processingStateIsSet()) {
            switch (processingState.getString("TYPE", "")) {
            case "NotPlanned":
                setSeverity(OrderStateText.PENDING);
                break;
            case "Planned":
                setSeverity(OrderStateText.PENDING);
                break;
            case "Setback":
                setSeverity(OrderStateText.SETBACK);
                break;
            case "InTaskProcess":
            case "OccupiedByClusterMember":
                setSeverity(OrderStateText.RUNNING);
            case "WaitingInTask":
                if (usedTasks != null) {
                    if (usedTasks.isWaitingForAgent(getTaskId())) {
                        setSeverity(OrderStateText.WAITING_FOR_AGENT); 
                    } else if (usedTasks.isWaitingForProcessClass(getTaskId())) {
                        setSeverity(OrderStateText.WAITING_FOR_PROCESS); 
                    }
                }
                break;
            case "Pending":
            case "Due":
            case "WaitingForResource":
            case "WaitingForOther":
                if (!processingStateIsSet() && usedNodes != null && usedNodes.getNode(getJobChain(), getState()) != null) {
                    if (usedNodes.getNode(getJobChain(), getState()).isStopped()) {
                        setSeverity(OrderStateText.NODE_STOPPED); 
                    } else if (usedNodes.getNode(getJobChain(), getState()).isWaitingForJob()) {
                        isWaitingForJob = true; 
                    }
                }
                break;
            case "Blacklisted":
                setSeverity(OrderStateText.BLACKLIST);
                break;
            default:
                break;
            }
        }
    }
    
    public boolean processingStateIsSet() {
        return getProcessingState() != null;
    }
    
    public void readJobObstacles(Job job) {
        if (job != null) {
            OrderStateText text = job.getState();
            if (text != null) {
                setSeverity(text);
                if (text == OrderStateText.JOB_NOT_IN_PERIOD) {
                    setNextStartTime(JobSchedulerDate.getDateFromISO8601String(job.nextPeriodBeginsAt()));
                }
                if (text == OrderStateText.WAITING_FOR_LOCK) {
                    setLock(job.getLock());
                } 
            }
        }
    }
    
    public void readJobChainObstacles(JobChain jobChain) {
        if (jobChain != null && jobChain.isStopped()) {
            setSeverity(OrderStateText.JOB_CHAIN_STOPPED);
        }
    }
    
    public void readTaskObstacles(Task task) {
        if (task != null && task.isWaitingForAgent()) {
            setSeverity(OrderStateText.WAITING_FOR_AGENT);
        }
    }
    
    public void setPathJobChainAndOrderId() throws JobSchedulerInvalidResponseDataException {
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
        String jobChainPath = overview.getString("jobChainPath", null);
        if (jobChainPath == null) {
            jobChainPath = pathParts[0];
        }
        setJobChain(jobChainPath);
        setOrderId(pathParts[1]);
        if (origJobChain == null) {
            origJobChain = pathParts[0];  
        }
    }
    
    private void cleanArrays() {
        setParams(null);
        setPriority(null);
    }
    
    private void setSeverity(OrderStateText text) {
        setProcessingState(new OrderState());
        getProcessingState().set_text(text);
        switch (text) {
        case PENDING:
            processingFilterState = OrderStateFilter.PENDING;
            getProcessingState().setSeverity(1);
            break;
        case RUNNING:
            processingFilterState = OrderStateFilter.RUNNING;
            getProcessingState().setSeverity(0);
            break;
        case SETBACK:
            processingFilterState = OrderStateFilter.SETBACK;
            getProcessingState().setSeverity(5);
            break;
        case SUSPENDED:
            processingFilterState = OrderStateFilter.SUSPENDED;
            getProcessingState().setSeverity(5);
            break;
        case WAITING_FOR_AGENT:
        case JOB_CHAIN_STOPPED:
        case JOB_STOPPED:
        case NODE_STOPPED:
            processingFilterState = OrderStateFilter.WAITINGFORRESOURCE;
            getProcessingState().setSeverity(2);
            break;
        case JOB_NOT_IN_PERIOD:
        case NODE_DELAY:
        case WAITING_FOR_LOCK:
        case WAITING_FOR_PROCESS:
        case WAITING_FOR_TASK:
            processingFilterState = OrderStateFilter.WAITINGFORRESOURCE;
            getProcessingState().setSeverity(3);
            break;
        case BLACKLIST:
            processingFilterState = OrderStateFilter.BLACKLIST;
            getProcessingState().setSeverity(3);
            break;
        }
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
                LOGGER.warn(String.format("Invalid responsed data: %1$s='%2$s'",fieldName, num.toString()));
            }
        }
        return null;
    }
    
    private String getStringFromIntField(JsonObject json, String fieldName) {
        Integer num = getIntField(json, fieldName);
        if (num != null) {
            return num.toString();
        }
        return null;
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
    
    private OrderType getType(String s) {
        switch(s) {
        case "AdHoc":
            return OrderType.AD_HOC;
        case "FileOrder":
            return OrderType.FILE_ORDER;
        case "Permanent":
            return OrderType.PERMANENT;
        default:
            return OrderType.PERMANENT;
        }
    }
    
    @JsonIgnore
    public OrderStateFilter getProcessingFilterState() {
        return processingFilterState;
    }

}
