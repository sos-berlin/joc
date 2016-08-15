
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sos.joc.model.common.ConfigurationStatusSchema;
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
    "job",
    "stateText",
    "configurationStatus",
    "processingState",
    "nextStartTime",
    "historyId",
    "startedAt",
    "processedBy",
    "taskId",
    "inProcessSince",
    "setback",
    "lock",
    "processClass"
})
public class OrderQueue {

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
    @JsonProperty("params")
    private List<Object> params = new ArrayList<Object>();
    /**
     * the type of the order
     * 
     */
    @JsonProperty("_type")
    private OrderQueue.Type type;
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
    private ConfigurationStatusSchema configurationStatus;
    @JsonProperty("processingState")
    private ProcessingState processingState;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("nextStartTime")
    private Date nextStartTime;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("historyId")
    private Integer historyId;
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
    private Integer taskId;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * 
     * @return
     *     The params
     */
    @JsonProperty("params")
    public List<Object> getParams() {
        return params;
    }

    /**
     * 
     * @param params
     *     The params
     */
    @JsonProperty("params")
    public void setParams(List<Object> params) {
        this.params = params;
    }

    /**
     * the type of the order
     * 
     * @return
     *     The type
     */
    @JsonProperty("_type")
    public OrderQueue.Type getType() {
        return type;
    }

    /**
     * the type of the order
     * 
     * @param type
     *     The _type
     */
    @JsonProperty("_type")
    public void setType(OrderQueue.Type type) {
        this.type = type;
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
    @JsonProperty("configurationStatus")
    public void setConfigurationStatus(ConfigurationStatusSchema configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    /**
     * 
     * @return
     *     The processingState
     */
    @JsonProperty("processingState")
    public ProcessingState getProcessingState() {
        return processingState;
    }

    /**
     * 
     * @param processingState
     *     The processingState
     */
    @JsonProperty("processingState")
    public void setProcessingState(ProcessingState processingState) {
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The historyId
     */
    @JsonProperty("historyId")
    public Integer getHistoryId() {
        return historyId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param historyId
     *     The historyId
     */
    @JsonProperty("historyId")
    public void setHistoryId(Integer historyId) {
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
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * ONLY for running order
     * 
     * @param taskId
     *     The taskId
     */
    @JsonProperty("taskId")
    public void setTaskId(Integer taskId) {
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
        return new HashCodeBuilder().append(path).append(orderId).append(jobChain).append(priority).append(params).append(type).append(surveyDate).append(state).append(job).append(stateText).append(configurationStatus).append(processingState).append(nextStartTime).append(historyId).append(startedAt).append(processedBy).append(taskId).append(inProcessSince).append(setback).append(lock).append(processClass).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderQueue) == false) {
            return false;
        }
        OrderQueue rhs = ((OrderQueue) other);
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(priority, rhs.priority).append(params, rhs.params).append(type, rhs.type).append(surveyDate, rhs.surveyDate).append(state, rhs.state).append(job, rhs.job).append(stateText, rhs.stateText).append(configurationStatus, rhs.configurationStatus).append(processingState, rhs.processingState).append(nextStartTime, rhs.nextStartTime).append(historyId, rhs.historyId).append(startedAt, rhs.startedAt).append(processedBy, rhs.processedBy).append(taskId, rhs.taskId).append(inProcessSince, rhs.inProcessSince).append(setback, rhs.setback).append(lock, rhs.lock).append(processClass, rhs.processClass).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        PERMANENT("permanent"),
        AD_HOC("ad_hoc"),
        FILE_ORDER("file_order");
        private final String value;
        private final static Map<String, OrderQueue.Type> CONSTANTS = new HashMap<String, OrderQueue.Type>();

        static {
            for (OrderQueue.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static OrderQueue.Type fromValue(String value) {
            OrderQueue.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
