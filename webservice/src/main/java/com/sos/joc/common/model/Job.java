
package com.sos.joc.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job object (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "survey_date",
    "path",
    "is_order_job",
    "next_period",
    "orders_summary",
    "order_queue",
    "next_start_time",
    "delay_until",
    "name",
    "all_tasks",
    "all_steps",
    "max_tasks",
    "state",
    "state_text",
    "process_class",
    "locks",
    "temporary",
    "num_of_running_tasks",
    "running_tasks",
    "num_of_queued_tasks",
    "task_queue",
    "configuration_status"
})
public class Job {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("survey_date")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private Pattern path;
    @JsonProperty("is_order_job")
    private Boolean isOrderJob;
    /**
     * only relevant for order jobs
     * 
     */
    @JsonProperty("next_period")
    private NextPeriod nextPeriod;
    /**
     * only relevant for order jobs
     * 
     */
    @JsonProperty("orders_summary")
    private OrdersSummary ordersSummary;
    /**
     * Only relevant for order jobs
     * 
     */
    @JsonProperty("order_queue")
    private List<OrderQueue> orderQueue = new ArrayList<OrderQueue>();
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("next_start_time")
    private Date nextStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("delay_until")
    private Date delayUntil;
    @JsonProperty("name")
    private String name;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("all_tasks")
    private Integer allTasks;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("all_steps")
    private Integer allSteps;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("max_tasks")
    private Integer maxTasks;
    @JsonProperty("state")
    private JobState state;
    @JsonProperty("state_text")
    private String stateText;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("process_class")
    private Pattern processClass;
    @JsonProperty("locks")
    private List<Lock> locks = new ArrayList<Lock>();
    @JsonProperty("temporary")
    private Boolean temporary = false;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("num_of_running_tasks")
    private Integer numOfRunningTasks;
    @JsonProperty("running_tasks")
    private List<RunningTask> runningTasks = new ArrayList<RunningTask>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("num_of_queued_tasks")
    private Integer numOfQueuedTasks;
    @JsonProperty("task_queue")
    private List<TaskQueue> taskQueue = new ArrayList<TaskQueue>();
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    @JsonProperty("configuration_status")
    private ConfigurationStatus configurationStatus;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("survey_date")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The survey_date
     */
    @JsonProperty("survey_date")
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
    public Pattern getPath() {
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
    public void setPath(Pattern path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The isOrderJob
     */
    @JsonProperty("is_order_job")
    public Boolean getIsOrderJob() {
        return isOrderJob;
    }

    /**
     * 
     * @param isOrderJob
     *     The is_order_job
     */
    @JsonProperty("is_order_job")
    public void setIsOrderJob(Boolean isOrderJob) {
        this.isOrderJob = isOrderJob;
    }

    /**
     * only relevant for order jobs
     * 
     * @return
     *     The nextPeriod
     */
    @JsonProperty("next_period")
    public NextPeriod getNextPeriod() {
        return nextPeriod;
    }

    /**
     * only relevant for order jobs
     * 
     * @param nextPeriod
     *     The next_period
     */
    @JsonProperty("next_period")
    public void setNextPeriod(NextPeriod nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

    /**
     * only relevant for order jobs
     * 
     * @return
     *     The ordersSummary
     */
    @JsonProperty("orders_summary")
    public OrdersSummary getOrdersSummary() {
        return ordersSummary;
    }

    /**
     * only relevant for order jobs
     * 
     * @param ordersSummary
     *     The orders_summary
     */
    @JsonProperty("orders_summary")
    public void setOrdersSummary(OrdersSummary ordersSummary) {
        this.ordersSummary = ordersSummary;
    }

    /**
     * Only relevant for order jobs
     * 
     * @return
     *     The orderQueue
     */
    @JsonProperty("order_queue")
    public List<OrderQueue> getOrderQueue() {
        return orderQueue;
    }

    /**
     * Only relevant for order jobs
     * 
     * @param orderQueue
     *     The order_queue
     */
    @JsonProperty("order_queue")
    public void setOrderQueue(List<OrderQueue> orderQueue) {
        this.orderQueue = orderQueue;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The nextStartTime
     */
    @JsonProperty("next_start_time")
    public Date getNextStartTime() {
        return nextStartTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param nextStartTime
     *     The next_start_time
     */
    @JsonProperty("next_start_time")
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
    @JsonProperty("delay_until")
    public Date getDelayUntil() {
        return delayUntil;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param delayUntil
     *     The delay_until
     */
    @JsonProperty("delay_until")
    public void setDelayUntil(Date delayUntil) {
        this.delayUntil = delayUntil;
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The allTasks
     */
    @JsonProperty("all_tasks")
    public Integer getAllTasks() {
        return allTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param allTasks
     *     The all_tasks
     */
    @JsonProperty("all_tasks")
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
    @JsonProperty("all_steps")
    public Integer getAllSteps() {
        return allSteps;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param allSteps
     *     The all_steps
     */
    @JsonProperty("all_steps")
    public void setAllSteps(Integer allSteps) {
        this.allSteps = allSteps;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The maxTasks
     */
    @JsonProperty("max_tasks")
    public Integer getMaxTasks() {
        return maxTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxTasks
     *     The max_tasks
     */
    @JsonProperty("max_tasks")
    public void setMaxTasks(Integer maxTasks) {
        this.maxTasks = maxTasks;
    }

    /**
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public JobState getState() {
        return state;
    }

    /**
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
    @JsonProperty("state_text")
    public String getStateText() {
        return stateText;
    }

    /**
     * 
     * @param stateText
     *     The state_text
     */
    @JsonProperty("state_text")
    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("process_class")
    public Pattern getProcessClass() {
        return processClass;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param processClass
     *     The process_class
     */
    @JsonProperty("process_class")
    public void setProcessClass(Pattern processClass) {
        this.processClass = processClass;
    }

    /**
     * 
     * @return
     *     The locks
     */
    @JsonProperty("locks")
    public List<Lock> getLocks() {
        return locks;
    }

    /**
     * 
     * @param locks
     *     The locks
     */
    @JsonProperty("locks")
    public void setLocks(List<Lock> locks) {
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
    @JsonProperty("num_of_running_tasks")
    public Integer getNumOfRunningTasks() {
        return numOfRunningTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfRunningTasks
     *     The num_of_running_tasks
     */
    @JsonProperty("num_of_running_tasks")
    public void setNumOfRunningTasks(Integer numOfRunningTasks) {
        this.numOfRunningTasks = numOfRunningTasks;
    }

    /**
     * 
     * @return
     *     The runningTasks
     */
    @JsonProperty("running_tasks")
    public List<RunningTask> getRunningTasks() {
        return runningTasks;
    }

    /**
     * 
     * @param runningTasks
     *     The running_tasks
     */
    @JsonProperty("running_tasks")
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
    @JsonProperty("num_of_queued_tasks")
    public Integer getNumOfQueuedTasks() {
        return numOfQueuedTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param numOfQueuedTasks
     *     The num_of_queued_tasks
     */
    @JsonProperty("num_of_queued_tasks")
    public void setNumOfQueuedTasks(Integer numOfQueuedTasks) {
        this.numOfQueuedTasks = numOfQueuedTasks;
    }

    /**
     * 
     * @return
     *     The taskQueue
     */
    @JsonProperty("task_queue")
    public List<TaskQueue> getTaskQueue() {
        return taskQueue;
    }

    /**
     * 
     * @param taskQueue
     *     The task_queue
     */
    @JsonProperty("task_queue")
    public void setTaskQueue(List<TaskQueue> taskQueue) {
        this.taskQueue = taskQueue;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @return
     *     The configurationStatus
     */
    @JsonProperty("configuration_status")
    public ConfigurationStatus getConfigurationStatus() {
        return configurationStatus;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @param configurationStatus
     *     The configuration_status
     */
    @JsonProperty("configuration_status")
    public void setConfigurationStatus(ConfigurationStatus configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(isOrderJob).append(nextPeriod).append(ordersSummary).append(orderQueue).append(nextStartTime).append(delayUntil).append(name).append(allTasks).append(allSteps).append(maxTasks).append(state).append(stateText).append(processClass).append(locks).append(temporary).append(numOfRunningTasks).append(runningTasks).append(numOfQueuedTasks).append(taskQueue).append(configurationStatus).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job) == false) {
            return false;
        }
        Job rhs = ((Job) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(isOrderJob, rhs.isOrderJob).append(nextPeriod, rhs.nextPeriod).append(ordersSummary, rhs.ordersSummary).append(orderQueue, rhs.orderQueue).append(nextStartTime, rhs.nextStartTime).append(delayUntil, rhs.delayUntil).append(name, rhs.name).append(allTasks, rhs.allTasks).append(allSteps, rhs.allSteps).append(maxTasks, rhs.maxTasks).append(state, rhs.state).append(stateText, rhs.stateText).append(processClass, rhs.processClass).append(locks, rhs.locks).append(temporary, rhs.temporary).append(numOfRunningTasks, rhs.numOfRunningTasks).append(runningTasks, rhs.runningTasks).append(numOfQueuedTasks, rhs.numOfQueuedTasks).append(taskQueue, rhs.taskQueue).append(configurationStatus, rhs.configurationStatus).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
