package com.sos.joc.classes.jobs;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.classes.orders.OrdersSnapshotCallable;
import com.sos.joc.classes.orders.OrdersSnapshotEvent;
import com.sos.joc.classes.orders.OrdersVCallable;
import com.sos.joc.classes.parameters.Parameters;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.job.JobState;
import com.sos.joc.model.job.JobStateText;
import com.sos.joc.model.job.JobV;
import com.sos.joc.model.job.OrderOfTask;
import com.sos.joc.model.job.QueuedTask;
import com.sos.joc.model.job.RunningTask;
import com.sos.joc.model.job.TaskCause;
import com.sos.joc.model.order.OrdersSummary;


public class JobVolatileJson extends JobV {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JobVolatileJson.class);
    private final JsonObject job;
    private final JsonObject overview;
    private final Boolean orderJob;
    private final Boolean withOrderQueue;
    private final Boolean compactView;
    private JOCResourceImpl jocResourceImpl;
    private String accessToken = null;
    private JsonObject summary = null;
    
    public JobVolatileJson(JsonObject job, Boolean compactView, JsonObject summary) {
        this.job = job;
        this.overview = getJobOverview();
        this.orderJob = this.overview.getBoolean("isOrderJob", false);
        this.withOrderQueue = false;
        this.summary = summary;
        this.compactView = compactView;
    }
    
    public JobVolatileJson(JsonObject job, Boolean compactView, Boolean withOrderQueue) {
        this.job = job;
        this.overview = getJobOverview();
        this.orderJob = this.overview.getBoolean("isOrderJob", false);
        this.withOrderQueue = withOrderQueue;
        this.summary = null;
        this.compactView = compactView;
    }
    
    public boolean isOrderJob() {
        return orderJob;
    }
    
    public void setPath() {
        if (getPath() == null) {
            setPath(this.overview.getString(WebserviceConstants.PATH, null));
            LOGGER.debug("...processing job: " + getPath());
        }
    }

    public void setState() {
        if (getState() == null) {
            JsonArray obstacles = overview.getJsonArray("obstacles");
            setNumOfRunningTasks(overview.getInt("usedTaskCount", 0));
            setNumOfQueuedTasks(overview.getInt("queuedTaskCount", 0));
            ConfigurationState confState = ConfigurationStatus.getConfigurationStatus(obstacles);
            setConfigurationStatus(confState);
            setState(new JobState());
            if (!overview.getBoolean("enabled", true)) {
                getState().set_text(JobStateText.DISABLED);
            } else if (confState != null && confState.getSeverity() == 2) {
                getState().set_text(JobStateText.ERROR);
            } else {
                try {
                    getState().set_text(JobStateText.fromValue(overview.getString("state", "UNKNOWN").toUpperCase()));
                } catch (Exception e) {
                    getState().set_text(JobStateText.fromValue("UNKNOWN"));
                }
                Optional<JsonValue> obstacleWaitingTypeObject = obstacles.stream().filter(p -> ((JsonObject) p).getString("TYPE", "").startsWith("Waiting")).findFirst();
                if (obstacleWaitingTypeObject.isPresent()) {
                    String obstacleWaitingType = ((JsonObject) obstacleWaitingTypeObject.get()).getString("TYPE");
                    if (obstacleWaitingType.equals("WaitingForLocks")) {
                        getState().set_text(JobStateText.WAITING_FOR_LOCK); 
                    } else if (obstacleWaitingType.equals("WaitingForProcessClass")) {
                        getState().set_text(JobStateText.WAITING_FOR_PROCESS);
                    }
                } else if (isWaitingForAgent()) {
                    getState().set_text(JobStateText.WAITING_FOR_AGENT);
                //} else if (false) { //TODO condition for WAITING_FOR_TASK
                    // TODO: WaitingForTask has to be improved
                    // Look into queue items where start_time in the past
                    // it could be that a task is queued caused of a delayed
                    // start instead of max tasks is reached
                //    getState().set_text(JobStateText.WAITING_FOR_TASK);
                } else if (isOrderJob() && getState().get_text() == JobStateText.PENDING && !overview.getBoolean("isInPeriod", true)) {
                    getState().set_text(JobStateText.NOT_IN_PERIOD);
                }
            }
            setSeverity(getState());
        }
    }
    
    private boolean isWaitingForAgent() {
        JsonObject taskObstacles = overview.getJsonObject("taskObstacles");
        if (taskObstacles != null) {
            for (JsonValue j : taskObstacles.values()) {
                if (j.getValueType() == ValueType.ARRAY) {
                    if (((JsonArray) j).stream().filter(p -> ((JsonObject) p).getString("TYPE", "").equals("WaitingForAgent")).findFirst().isPresent()) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }
    
    //JOC-89, order job is pending for some states if node doesn't have a running order
    public void setState(boolean hasRunningOrWaitingOrder) {
        if (getState() == null) {
            setState();
        }
        if (!hasRunningOrWaitingOrder) {
            switch (getState().get_text()) {
            case NOT_IN_PERIOD:
            case RUNNING:
            case WAITING_FOR_AGENT:
            case WAITING_FOR_LOCK:
            case WAITING_FOR_PROCESS:
            case WAITING_FOR_TASK:
                JobState state = new JobState();
                state.set_text(JobStateText.PENDING);
                state.setSeverity(1);
                setState(state);
                //setRunningTasks(null);
                //setNumOfRunningTasks(0);
            default:
                break;
            }
        }
    }
    
    public void setFields(boolean compact, JOCResourceImpl jocResourceImpl, String accessToken) throws JocException {
        this.accessToken = accessToken;
        this.jocResourceImpl = jocResourceImpl;
        if (compact) {
            setCompactFields();
            cleanArrays();
        } else {
            setDetailedFields();
        }
    }

    private void cleanArrays() {
        setParams(null);
        setTaskQueue(null);
        setRunningTasks(null);
        setOrderQueue(null);
    }

    private void setDetailedFields() throws JocException {
        setCompactFields();
        setAllSteps(0);
        setParams(Parameters.getParameters(job, "defaultParameters"));
        setTaskQueue();
        setRunningTasks();
        setTemporary(null);
        //TODO check if field is correct
        if (isOrderJob()) {
            setDelayUntil(null);
        } else {
            setDelayUntil(JobSchedulerDate.getDateFromISO8601String(job.getString("delayAfterError", Instant.EPOCH.toString())));
        }
    }

    private void setCompactFields() throws JocException {
        setPath();
        setState();
        setName(Paths.get(getPath()).getFileName().toString());
        setLocks(null); //is not displayed in JOC Cockpit
        setStateText(overview.getString("stateText", ""));
        setNextStartTime(JobSchedulerDate.getDateFromISO8601String(overview.getString("nextStartTime", Instant.EPOCH.toString())));
        if (!isOrderJob() && getNextStartTime() == null) {
            setNextStartNever(true);
        }
        setErr(overview.getJsonObject("error"));
        setSummary();
        if (isOrderJob() && withOrderQueue) {
            JOCJsonCommand jocJsonCommand = new JOCJsonCommand(jocResourceImpl);
            jocJsonCommand.setUriBuilderForOrders();
            jocJsonCommand.addOrderCompactQuery(false);
            setOrderQueue(new OrdersVCallable(getPath(), false, jocJsonCommand, accessToken).getOrdersOfJob());
        } else {
            setOrderQueue(null);
        }
    }
    
    private void setErr(JsonObject errorElem) {
        if (errorElem != null) {
            Err err = new Err();
            err.setCode(errorElem.getString("code", null));
            err.setMessage(errorElem.getString("message", null));
            setError(err);
        }
    }

    private void setSeverity(JobState state) {
        switch (state.get_text()) {
        case RUNNING:
            state.setSeverity(0);
            break;
        case PENDING:
            state.setSeverity(1);
            break;
        case STOPPED:
            state.setSeverity(2);
            state.setManually(false);
            break;
        case NOT_INITIALIZED:
        case WAITING_FOR_AGENT:
        case STOPPING:
        case ERROR:
            state.setSeverity(2);
            break;
        case INITIALIZED:
        case LOADED:
        case WAITING_FOR_PROCESS:
        case WAITING_FOR_LOCK:
        case WAITING_FOR_TASK:
        case NOT_IN_PERIOD:
            state.setSeverity(3);
            break;
        case DISABLED:
        case UNKNOWN:
            state.setSeverity(4);
            break;
        }
    }
    
    private void setTaskQueue() {
        JsonArray taskList = job.getJsonArray("queuedTasks");
        if (taskList != null && !taskList.isEmpty()) {
            List<QueuedTask> queuedTasks = new ArrayList<QueuedTask>(); 
            for (JsonObject queuedTask : taskList.getValuesAs(JsonObject.class)) {
                QueuedTask taskQueue = new QueuedTask();
                taskQueue.setTaskId(queuedTask.getString("taskId", null));
                taskQueue.setEnqueued(JobSchedulerDate.getDateFromISO8601String(queuedTask.getString("enqueuedAt", Instant.EPOCH.toString())));
                taskQueue.setPlannedStart(JobSchedulerDate.getDateFromISO8601String(queuedTask.getString("startAt", Instant.EPOCH.toString())));
                queuedTasks.add(taskQueue);
            }
            setTaskQueue(queuedTasks);
        } else {
            setTaskQueue(null);
        }
    }
    
    private void setRunningTasks() {
        JsonArray taskList = job.getJsonArray("runningTasks");
        if (taskList != null && !taskList.isEmpty()) {
            List<RunningTask> runningTasks = new ArrayList<RunningTask>();
            for (JsonObject runningTask : taskList.getValuesAs(JsonObject.class)) {
                RunningTask task = new RunningTask();
                try {
                    task.set_cause(TaskCause.fromValue(runningTask.getString("cause", "").toUpperCase()));
                } catch (Exception e) {
                }
                task.setIdleSince(null); //not displayed in JOC, missing in JSON answer
                task.setStartedAt(JobSchedulerDate.getDateFromISO8601String(runningTask.getString("startedAt", Instant.EPOCH.toString())));
                task.setEnqueued(JobSchedulerDate.getDateFromISO8601String(runningTask.getString("enqueuedAt", Instant.EPOCH.toString()))); 
                //task.setPlannedStart(JobSchedulerDate.getDateFromISO8601String(runningTask.getString("startAt", Instant.EPOCH.toString())));
                if (task.getEnqueued() == null) {
                    task.setEnqueued(task.getStartedAt()); 
                }
                task.setPid(runningTask.getInt("pid", 0));
                task.setSteps(0); //TODO missing in JSON answer
                task.setTaskId(runningTask.getString("taskId", null));
                JsonObject jsonOrder = runningTask.getJsonObject("order");
                if (jsonOrder != null) {
                    OrderOfTask order = new OrderOfTask();
                    order.setInProcessSince(null); //not displayed in JOC, missing in JSON answer
                    order.setJobChain(jsonOrder.getString("jobChainPath", null));
                    order.setOrderId(jsonOrder.getString("orderId", null));
                    order.setPath(order.getJobChain()+","+order.getOrderId());
                    order.setState(null); //not displayed in JOC, missing in JSON answer
                    order.setTitle(null); //not displayed in JOC, missing in JSON answer
                    task.setOrder(order);
                }
                runningTasks.add(task);
            }
            setRunningTasks(runningTasks);
        } else {
            setRunningTasks((List<RunningTask>) null);
        }
    }
    
    private void setSummary() throws JocException {
        if (isOrderJob() && compactView != Boolean.TRUE) {
            JsonObject j = null;
            if (summary != null) {
                j = summary.getJsonObject(this.getPath());
            }
            if (j != null) {
                OrdersSummary ordersSummary = new OrdersSummary();
                ordersSummary.setBlacklist(j.getInt("blacklisted", 0));
                ordersSummary.setPending(j.getInt("notPlanned", 0) + j.getInt("planned", 0));
                ordersSummary.setSetback(j.getInt("setback", 0));
                ordersSummary.setRunning(j.getInt("inTaskProcess", 0) + j.getInt("occupiedByClusterMember", 0));
                ordersSummary.setSuspended(j.getInt("suspended", 0));
                ordersSummary.setWaitingForResource(j.getInt("waitingForResource", 0) + j.getInt("due", 0) + j.getInt("inTask", 0) - j.getInt(
                        "inTaskProcess", 0));
                setOrdersSummary(ordersSummary);
            } else {
                JOCJsonCommand jocJsonCommand = new JOCJsonCommand(jocResourceImpl);
                jocJsonCommand.setUriBuilderForJobs();
                jocJsonCommand.addOrderStatisticsQuery(false);
                OrdersSnapshotEvent o = new OrdersSnapshotCallable(getPath(), jocJsonCommand, accessToken).getOrdersSnapshot();
                OrdersSummary ordersSummary = new OrdersSummary();
                ordersSummary.setBlacklist(o.getBlacklist());
                ordersSummary.setPending(o.getPending());
                ordersSummary.setSetback(o.getSetback());
                ordersSummary.setRunning(o.getRunning());
                ordersSummary.setSuspended(o.getSuspended());
                ordersSummary.setWaitingForResource(o.getWaitingForResource());
                setOrdersSummary(ordersSummary);
            }
        }
    }
    
    private JsonObject getJobOverview() {
        return job.containsKey("overview") ? job.getJsonObject("overview") : job;
    }
}
