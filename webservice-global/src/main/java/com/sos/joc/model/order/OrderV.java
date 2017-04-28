
package com.sos.joc.model.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.NameValuePair;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "path",
    "orderId",
    "jobChain",
    "priority",
    "params",
    "_type",
    "surveyDate",
    "state",
    "title",
    "job",
    "stateText",
    "configurationStatus",
    "endState",
    "processingState",
    "nextStartTime",
    "historyId",
    "startedAt",
    "processedBy",
    "taskId",
    "inProcessSince",
    "setback",
    "lock",
    "processClass",
    "runTimeIsTemporary"
})
public class OrderV {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;
    @JsonProperty("orderId")
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("jobChain")
    private String jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("priority")
    private Integer priority;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    private List<NameValuePair> params = new ArrayList<NameValuePair>();
    /**
     * order type
     * <p>
     * the type of the order
     * 
     */
    @JsonProperty("_type")
    private OrderType _type;
    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * the name of the node
     * 
     */
    @JsonProperty("state")
    private String state;
    @JsonProperty("title")
    private String title;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("job")
    private String job;
    @JsonProperty("stateText")
    private String stateText;
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    @JsonProperty("configurationStatus")
    private ConfigurationState configurationStatus;
    /**
     * the name of the end node
     * 
     */
    @JsonProperty("endState")
    private String endState;
    /**
     * jobChain state
     * <p>
     * 
     * 
     */
    @JsonProperty("processingState")
    private OrderState processingState;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("nextStartTime")
    private Date nextStartTime;
    /**
     * for all orders except pending orders
     * 
     */
    @JsonProperty("historyId")
    private String historyId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startedAt")
    private Date startedAt;
    /**
     * ONLY for running or blacklist order, contains Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     */
    @JsonProperty("processedBy")
    private String processedBy;
    /**
     * ONLY for running order
     * 
     */
    @JsonProperty("taskId")
    private String taskId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("inProcessSince")
    private Date inProcessSince;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("setback")
    private Date setback;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("lock")
    private String lock;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("processClass")
    private String processClass;
    @JsonProperty("runTimeIsTemporary")
    private Boolean runTimeIsTemporary = false;

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
     *     The orderId
     */
    @JsonProperty("orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId
     *     The orderId
     */
    @JsonProperty("orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    public Integer getPriority() {
        return priority;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    public void setPriority(Integer priority) {
        this.priority = priority;
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
     * order type
     * <p>
     * the type of the order
     * 
     * @return
     *     The _type
     */
    @JsonProperty("_type")
    public OrderType get_type() {
        return _type;
    }

    /**
     * order type
     * <p>
     * the type of the order
     * 
     * @param _type
     *     The _type
     */
    @JsonProperty("_type")
    public void set_type(OrderType _type) {
        this._type = _type;
    }

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
     * the name of the node
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * the name of the node
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(String job) {
        this.job = job;
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
     * the name of the end node
     * 
     * @return
     *     The endState
     */
    @JsonProperty("endState")
    public String getEndState() {
        return endState;
    }

    /**
     * the name of the end node
     * 
     * @param endState
     *     The endState
     */
    @JsonProperty("endState")
    public void setEndState(String endState) {
        this.endState = endState;
    }

    /**
     * jobChain state
     * <p>
     * 
     * 
     * @return
     *     The processingState
     */
    @JsonProperty("processingState")
    public OrderState getProcessingState() {
        return processingState;
    }

    /**
     * jobChain state
     * <p>
     * 
     * 
     * @param processingState
     *     The processingState
     */
    @JsonProperty("processingState")
    public void setProcessingState(OrderState processingState) {
        this.processingState = processingState;
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
     * for all orders except pending orders
     * 
     * @return
     *     The historyId
     */
    @JsonProperty("historyId")
    public String getHistoryId() {
        return historyId;
    }

    /**
     * for all orders except pending orders
     * 
     * @param historyId
     *     The historyId
     */
    @JsonProperty("historyId")
    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The startedAt
     */
    @JsonProperty("startedAt")
    public Date getStartedAt() {
        return startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param startedAt
     *     The startedAt
     */
    @JsonProperty("startedAt")
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * ONLY for running or blacklist order, contains Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     * @return
     *     The processedBy
     */
    @JsonProperty("processedBy")
    public String getProcessedBy() {
        return processedBy;
    }

    /**
     * ONLY for running or blacklist order, contains Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     * @param processedBy
     *     The processedBy
     */
    @JsonProperty("processedBy")
    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    /**
     * ONLY for running order
     * 
     * @return
     *     The taskId
     */
    @JsonProperty("taskId")
    public String getTaskId() {
        return taskId;
    }

    /**
     * ONLY for running order
     * 
     * @param taskId
     *     The taskId
     */
    @JsonProperty("taskId")
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The inProcessSince
     */
    @JsonProperty("inProcessSince")
    public Date getInProcessSince() {
        return inProcessSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param inProcessSince
     *     The inProcessSince
     */
    @JsonProperty("inProcessSince")
    public void setInProcessSince(Date inProcessSince) {
        this.inProcessSince = inProcessSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The setback
     */
    @JsonProperty("setback")
    public Date getSetback() {
        return setback;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param setback
     *     The setback
     */
    @JsonProperty("setback")
    public void setSetback(Date setback) {
        this.setback = setback;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The lock
     */
    @JsonProperty("lock")
    public String getLock() {
        return lock;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param lock
     *     The lock
     */
    @JsonProperty("lock")
    public void setLock(String lock) {
        this.lock = lock;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("processClass")
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param processClass
     *     The processClass
     */
    @JsonProperty("processClass")
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
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
        return new HashCodeBuilder().append(path).append(orderId).append(jobChain).append(priority).append(params).append(_type).append(surveyDate).append(state).append(title).append(job).append(stateText).append(configurationStatus).append(endState).append(processingState).append(nextStartTime).append(historyId).append(startedAt).append(processedBy).append(taskId).append(inProcessSince).append(setback).append(lock).append(processClass).append(runTimeIsTemporary).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderV) == false) {
            return false;
        }
        OrderV rhs = ((OrderV) other);
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(priority, rhs.priority).append(params, rhs.params).append(_type, rhs._type).append(surveyDate, rhs.surveyDate).append(state, rhs.state).append(title, rhs.title).append(job, rhs.job).append(stateText, rhs.stateText).append(configurationStatus, rhs.configurationStatus).append(endState, rhs.endState).append(processingState, rhs.processingState).append(nextStartTime, rhs.nextStartTime).append(historyId, rhs.historyId).append(startedAt, rhs.startedAt).append(processedBy, rhs.processedBy).append(taskId, rhs.taskId).append(inProcessSince, rhs.inProcessSince).append(setback, rhs.setback).append(lock, rhs.lock).append(processClass, rhs.processClass).append(runTimeIsTemporary, rhs.runTimeIsTemporary).isEquals();
    }

}
