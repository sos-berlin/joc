
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.NameValuePairsSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class OrderQueue {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String path;
    private String orderId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer priority;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    private List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
    /**
     * the type of the order
     * 
     */
    private OrderQueue.Type type;
    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date surveyDate;
    /**
     * the name of the node
     * 
     */
    private String state;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String job;
    private String stateText;
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    private ConfigurationStatusSchema configurationStatus;
    /**
     * the name of the end node
     * 
     */
    private String endState;
    /**
     * 
     */
    private ProcessingState processingState;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date nextStartTime;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer historyId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date startedAt;
    /**
     * ONLY for running or blacklist order, contains Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     */
    private String processedBy;
    /**
     * ONLY for running order
     * 
     */
    private Integer taskId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date inProcessSince;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date setback;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String lock;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String processClass;

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
     *     The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId
     *     The orderId
     */
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
     * the type of the order
     * 
     * @return
     *     The type
     */
    public OrderQueue.Type getType() {
        return type;
    }

    /**
     * the type of the order
     * 
     * @param type
     *     The _type
     */
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
     * the name of the node
     * 
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     * the name of the node
     * 
     * @param state
     *     The state
     */
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
    public void setJob(String job) {
        this.job = job;
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
     * the name of the end node
     * 
     * @return
     *     The endState
     */
    public String getEndState() {
        return endState;
    }

    /**
     * the name of the end node
     * 
     * @param endState
     *     The endState
     */
    public void setEndState(String endState) {
        this.endState = endState;
    }

    /**
     * 
     * @return
     *     The processingState
     */
    public ProcessingState getProcessingState() {
        return processingState;
    }

    /**
     * 
     * @param processingState
     *     The processingState
     */
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The historyId
     */
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
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * ONLY for running or blacklist order, contains Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     * @return
     *     The processedBy
     */
    public String getProcessedBy() {
        return processedBy;
    }

    /**
     * ONLY for running or blacklist order, contains Host/port of an active cluster member or URL of a JobScheduler Agent
     * 
     * @param processedBy
     *     The processedBy
     */
    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    /**
     * ONLY for running order
     * 
     * @return
     *     The taskId
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * ONLY for running order
     * 
     * @param taskId
     *     The taskId
     */
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
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(orderId).append(jobChain).append(priority).append(params).append(type).append(surveyDate).append(state).append(job).append(stateText).append(configurationStatus).append(endState).append(processingState).append(nextStartTime).append(historyId).append(startedAt).append(processedBy).append(taskId).append(inProcessSince).append(setback).append(lock).append(processClass).toHashCode();
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
        return new EqualsBuilder().append(path, rhs.path).append(orderId, rhs.orderId).append(jobChain, rhs.jobChain).append(priority, rhs.priority).append(params, rhs.params).append(type, rhs.type).append(surveyDate, rhs.surveyDate).append(state, rhs.state).append(job, rhs.job).append(stateText, rhs.stateText).append(configurationStatus, rhs.configurationStatus).append(endState, rhs.endState).append(processingState, rhs.processingState).append(nextStartTime, rhs.nextStartTime).append(historyId, rhs.historyId).append(startedAt, rhs.startedAt).append(processedBy, rhs.processedBy).append(taskId, rhs.taskId).append(inProcessSince, rhs.inProcessSince).append(setback, rhs.setback).append(lock, rhs.lock).append(processClass, rhs.processClass).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        PERMANENT("PERMANENT"),
        AD_HOC("AD_HOC"),
        FILE_ORDER("FILE_ORDER");
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

        @Override
        public String toString() {
            return this.value;
        }

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
