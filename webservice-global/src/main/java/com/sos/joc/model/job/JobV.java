
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.order.OrdersSummary;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "name",
    "orderQueue",
    "allTasks",
    "allSteps",
    "state",
    "stateText",
    "locks",
    "temporary",
    "numOfRunningTasks",
    "runningTasks",
    "numOfQueuedTasks",
    "taskQueue",
    "params",
    "configurationStatus",
    "error",
    "ordersSummary",
    "nextStartTime",
    "delayUntil",
    "runTimeIsTemporary"
})
public class JobV {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;
    @JsonProperty("name")
    private String name;
    /**
     * Only for /job/orderQueue
     * 
     */
    @JsonProperty("orderQueue")
    private List<OrderV> orderQueue = new ArrayList<OrderV>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("allTasks")
    private Integer allTasks;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("allSteps")
    private Integer allSteps;
    /**
     * job state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private JobState state;
    @JsonProperty("stateText")
    private String stateText;
    /**
     * job locks (volatile)
     * <p>
     * 
     * 
     */
    @JsonProperty("locks")
    private List<LockUseV> locks = new ArrayList<LockUseV>();
    @JsonProperty("temporary")
    private Boolean temporary;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfRunningTasks")
    private Integer numOfRunningTasks;
    @JsonProperty("runningTasks")
    private List<RunningTask> runningTasks = new ArrayList<RunningTask>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfQueuedTasks")
    private Integer numOfQueuedTasks;
    @JsonProperty("taskQueue")
    private List<QueuedTask> taskQueue = new ArrayList<QueuedTask>();
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    @JsonProperty("configurationStatus")
    private ConfigurationState configurationStatus;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     */
    @JsonProperty("ordersSummary")
    private OrdersSummary ordersSummary;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("nextStartTime")
    private Date nextStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("delayUntil")
    private Date delayUntil;
    @JsonProperty("runTimeIsTemporary")
    private Boolean runTimeIsTemporary = false;

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Only for /job/orderQueue
     * 
     * @return
     *     The orderQueue
     */
    @JsonProperty("orderQueue")
    public List<OrderV> getOrderQueue() {
        return orderQueue;
    }

    /**
     * Only for /job/orderQueue
     * 
     * @param orderQueue
     *     The orderQueue
     */
    @JsonProperty("orderQueue")
    public void setOrderQueue(List<OrderV> orderQueue) {
        this.orderQueue = orderQueue;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The allTasks
     */
    @JsonProperty("allTasks")
    public Integer getAllTasks() {
        return allTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param allTasks
     *     The allTasks
     */
    @JsonProperty("allTasks")
    public void setAllTasks(Integer allTasks) {
        this.allTasks = allTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The allSteps
     */
    @JsonProperty("allSteps")
    public Integer getAllSteps() {
        return allSteps;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param allSteps
     *     The allSteps
     */
    @JsonProperty("allSteps")
    public void setAllSteps(Integer allSteps) {
        this.allSteps = allSteps;
    }

    /**
     * job state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public JobState getState() {
        return state;
    }

    /**
     * job state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(JobState state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The stateText
     */
    @JsonProperty("stateText")
    public String getStateText() {
        return stateText;
    }

    /**
     * 
     * @param stateText
     *     The stateText
     */
    @JsonProperty("stateText")
    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    /**
     * job locks (volatile)
     * <p>
     * 
     * 
     * @return
     *     The locks
     */
    @JsonProperty("locks")
    public List<LockUseV> getLocks() {
        return locks;
    }

    /**
     * job locks (volatile)
     * <p>
     * 
     * 
     * @param locks
     *     The locks
     */
    @JsonProperty("locks")
    public void setLocks(List<LockUseV> locks) {
        this.locks = locks;
    }

    /**
     * 
     * @return
     *     The temporary
     */
    @JsonProperty("temporary")
    public Boolean getTemporary() {
        return temporary;
    }

    /**
     * 
     * @param temporary
     *     The temporary
     */
    @JsonProperty("temporary")
    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfRunningTasks
     */
    @JsonProperty("numOfRunningTasks")
    public Integer getNumOfRunningTasks() {
        return numOfRunningTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfRunningTasks
     *     The numOfRunningTasks
     */
    @JsonProperty("numOfRunningTasks")
    public void setNumOfRunningTasks(Integer numOfRunningTasks) {
        this.numOfRunningTasks = numOfRunningTasks;
    }

    /**
     * 
     * @return
     *     The runningTasks
     */
    @JsonProperty("runningTasks")
    public List<RunningTask> getRunningTasks() {
        return runningTasks;
    }

    /**
     * 
     * @param runningTasks
     *     The runningTasks
     */
    @JsonProperty("runningTasks")
    public void setRunningTasks(List<RunningTask> runningTasks) {
        this.runningTasks = runningTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The numOfQueuedTasks
     */
    @JsonProperty("numOfQueuedTasks")
    public Integer getNumOfQueuedTasks() {
        return numOfQueuedTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfQueuedTasks
     *     The numOfQueuedTasks
     */
    @JsonProperty("numOfQueuedTasks")
    public void setNumOfQueuedTasks(Integer numOfQueuedTasks) {
        this.numOfQueuedTasks = numOfQueuedTasks;
    }

    /**
     * 
     * @return
     *     The taskQueue
     */
    @JsonProperty("taskQueue")
    public List<QueuedTask> getTaskQueue() {
        return taskQueue;
    }

    /**
     * 
     * @param taskQueue
     *     The taskQueue
     */
    @JsonProperty("taskQueue")
    public void setTaskQueue(List<QueuedTask> taskQueue) {
        this.taskQueue = taskQueue;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @return
     *     The params
     */
    @JsonProperty("params")
    public List<NameValuePair> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @param params
     *     The params
     */
    @JsonProperty("params")
    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @return
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public ConfigurationState getConfigurationStatus() {
        return configurationStatus;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @param configurationStatus
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public void setConfigurationStatus(ConfigurationState configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @return
     *     The error
     */
    @JsonProperty("error")
    public Err getError() {
        return error;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @param error
     *     The error
     */
    @JsonProperty("error")
    public void setError(Err error) {
        this.error = error;
    }

    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     * @return
     *     The ordersSummary
     */
    @JsonProperty("ordersSummary")
    public OrdersSummary getOrdersSummary() {
        return ordersSummary;
    }

    /**
     * job chain order summary
     * <p>
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     * @param ordersSummary
     *     The ordersSummary
     */
    @JsonProperty("ordersSummary")
    public void setOrdersSummary(OrdersSummary ordersSummary) {
        this.ordersSummary = ordersSummary;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The nextStartTime
     */
    @JsonProperty("nextStartTime")
    public Date getNextStartTime() {
        return nextStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param nextStartTime
     *     The nextStartTime
     */
    @JsonProperty("nextStartTime")
    public void setNextStartTime(Date nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The delayUntil
     */
    @JsonProperty("delayUntil")
    public Date getDelayUntil() {
        return delayUntil;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param delayUntil
     *     The delayUntil
     */
    @JsonProperty("delayUntil")
    public void setDelayUntil(Date delayUntil) {
        this.delayUntil = delayUntil;
    }

    /**
     * 
     * @return
     *     The runTimeIsTemporary
     */
    @JsonProperty("runTimeIsTemporary")
    public Boolean getRunTimeIsTemporary() {
        return runTimeIsTemporary;
    }

    /**
     * 
     * @param runTimeIsTemporary
     *     The runTimeIsTemporary
     */
    @JsonProperty("runTimeIsTemporary")
    public void setRunTimeIsTemporary(Boolean runTimeIsTemporary) {
        this.runTimeIsTemporary = runTimeIsTemporary;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(orderQueue).append(allTasks).append(allSteps).append(state).append(stateText).append(locks).append(temporary).append(numOfRunningTasks).append(runningTasks).append(numOfQueuedTasks).append(taskQueue).append(params).append(configurationStatus).append(error).append(ordersSummary).append(nextStartTime).append(delayUntil).append(runTimeIsTemporary).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobV) == false) {
            return false;
        }
        JobV rhs = ((JobV) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(orderQueue, rhs.orderQueue).append(allTasks, rhs.allTasks).append(allSteps, rhs.allSteps).append(state, rhs.state).append(stateText, rhs.stateText).append(locks, rhs.locks).append(temporary, rhs.temporary).append(numOfRunningTasks, rhs.numOfRunningTasks).append(runningTasks, rhs.runningTasks).append(numOfQueuedTasks, rhs.numOfQueuedTasks).append(taskQueue, rhs.taskQueue).append(params, rhs.params).append(configurationStatus, rhs.configurationStatus).append(error, rhs.error).append(ordersSummary, rhs.ordersSummary).append(nextStartTime, rhs.nextStartTime).append(delayUntil, rhs.delayUntil).append(runTimeIsTemporary, rhs.runTimeIsTemporary).isEquals();
    }

}
