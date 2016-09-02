
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Job_ {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String path;
    private String name;
    /**
     * only relevant for order jobs if state.text=not_in_period
     * 
     */
    private String nextPeriodBegin;
    /**
     * Only for /job/orderQueue
     * 
     */
    private List<OrderQueue> orderQueue = new ArrayList<OrderQueue>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer allTasks;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer allSteps;
    /**
     * 
     */
    private State_ state;
    private String stateText;
    private List<Lock_> locks = new ArrayList<Lock_>();
    private Boolean temporary;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfRunningTasks;
    private List<RunningTask> runningTasks = new ArrayList<RunningTask>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer numOfQueuedTasks;
    private List<TaskQueue> taskQueue = new ArrayList<TaskQueue>();
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    private List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    private ConfigurationStatusSchema configurationStatus;
    /**
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     */
    private OrdersSummary ordersSummary;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date nextStartTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date delayUntil;

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
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
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * only relevant for order jobs if state.text=not_in_period
     * 
     * @return
     *     The nextPeriodBegin
     */
    public String getNextPeriodBegin() {
        return nextPeriodBegin;
    }

    /**
     * only relevant for order jobs if state.text=not_in_period
     * 
     * @param nextPeriodBegin
     *     The nextPeriodBegin
     */
    public void setNextPeriodBegin(String nextPeriodBegin) {
        this.nextPeriodBegin = nextPeriodBegin;
    }

    /**
     * Only for /job/orderQueue
     * 
     * @return
     *     The orderQueue
     */
    public List<OrderQueue> getOrderQueue() {
        return orderQueue;
    }

    /**
     * Only for /job/orderQueue
     * 
     * @param orderQueue
     *     The orderQueue
     */
    public void setOrderQueue(List<OrderQueue> orderQueue) {
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
    public void setAllSteps(Integer allSteps) {
        this.allSteps = allSteps;
    }

    /**
     * 
     * @return
     *     The state
     */
    public State_ getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(State_ state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The stateText
     */
    public String getStateText() {
        return stateText;
    }

    /**
     * 
     * @param stateText
     *     The stateText
     */
    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    /**
     * 
     * @return
     *     The locks
     */
    public List<Lock_> getLocks() {
        return locks;
    }

    /**
     * 
     * @param locks
     *     The locks
     */
    public void setLocks(List<Lock_> locks) {
        this.locks = locks;
    }

    /**
     * 
     * @return
     *     The temporary
     */
    public Boolean getTemporary() {
        return temporary;
    }

    /**
     * 
     * @param temporary
     *     The temporary
     */
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
    public void setNumOfRunningTasks(Integer numOfRunningTasks) {
        this.numOfRunningTasks = numOfRunningTasks;
    }

    /**
     * 
     * @return
     *     The runningTasks
     */
    public List<RunningTask> getRunningTasks() {
        return runningTasks;
    }

    /**
     * 
     * @param runningTasks
     *     The runningTasks
     */
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
    public void setNumOfQueuedTasks(Integer numOfQueuedTasks) {
        this.numOfQueuedTasks = numOfQueuedTasks;
    }

    /**
     * 
     * @return
     *     The taskQueue
     */
    public List<TaskQueue> getTaskQueue() {
        return taskQueue;
    }

    /**
     * 
     * @param taskQueue
     *     The taskQueue
     */
    public void setTaskQueue(List<TaskQueue> taskQueue) {
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
    public List<NameValuePairsSchema> getParams() {
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
    public void setParams(List<NameValuePairsSchema> params) {
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
    public ConfigurationStatusSchema getConfigurationStatus() {
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
    public void setConfigurationStatus(ConfigurationStatusSchema configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    /**
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     * @return
     *     The ordersSummary
     */
    public OrdersSummary getOrdersSummary() {
        return ordersSummary;
    }

    /**
     * only relevant for order jobs and is empty if job's order queue is empty
     * 
     * @param ordersSummary
     *     The ordersSummary
     */
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
    public void setDelayUntil(Date delayUntil) {
        this.delayUntil = delayUntil;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(nextPeriodBegin).append(orderQueue).append(allTasks).append(allSteps).append(state).append(stateText).append(locks).append(temporary).append(numOfRunningTasks).append(runningTasks).append(numOfQueuedTasks).append(taskQueue).append(params).append(configurationStatus).append(ordersSummary).append(nextStartTime).append(delayUntil).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job_) == false) {
            return false;
        }
        Job_ rhs = ((Job_) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(nextPeriodBegin, rhs.nextPeriodBegin).append(orderQueue, rhs.orderQueue).append(allTasks, rhs.allTasks).append(allSteps, rhs.allSteps).append(state, rhs.state).append(stateText, rhs.stateText).append(locks, rhs.locks).append(temporary, rhs.temporary).append(numOfRunningTasks, rhs.numOfRunningTasks).append(runningTasks, rhs.runningTasks).append(numOfQueuedTasks, rhs.numOfQueuedTasks).append(taskQueue, rhs.taskQueue).append(params, rhs.params).append(configurationStatus, rhs.configurationStatus).append(ordersSummary, rhs.ordersSummary).append(nextStartTime, rhs.nextStartTime).append(delayUntil, rhs.delayUntil).isEquals();
    }

}
