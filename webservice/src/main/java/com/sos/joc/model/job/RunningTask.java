
package com.sos.joc.model.job;

import java.util.Date;
import java.util.HashMap;
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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * running task
 * <p>
 * task object of an order job which is running
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "taskId",
    "pid",
    "startedAt",
    "enqueued",
    "idleSince",
    "steps",
    "cause",
    "order"
})
public class RunningTask {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    private Integer taskId;
    @JsonProperty("pid")
    private Integer pid;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("startedAt")
    private Date startedAt;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("enqueued")
    private Date enqueued;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("idleSince")
    private Date idleSince;
    @JsonProperty("steps")
    private Integer steps;
    /**
     * For order jobs only cause=order possible
     * 
     */
    @JsonProperty("cause")
    private RunningTask.Cause cause;
    /**
     * Only relevant for order jobs; cause=order resp.
     * 
     */
    @JsonProperty("order")
    private Order order;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The taskId
     */
    @JsonProperty("taskId")
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 
     * (Required)
     * 
     * @param taskId
     *     The taskId
     */
    @JsonProperty("taskId")
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 
     * @return
     *     The pid
     */
    @JsonProperty("pid")
    public Integer getPid() {
        return pid;
    }

    /**
     * 
     * @param pid
     *     The pid
     */
    @JsonProperty("pid")
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
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
     * (Required)
     * 
     * @param startedAt
     *     The startedAt
     */
    @JsonProperty("startedAt")
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The enqueued
     */
    @JsonProperty("enqueued")
    public Date getEnqueued() {
        return enqueued;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param enqueued
     *     The enqueued
     */
    @JsonProperty("enqueued")
    public void setEnqueued(Date enqueued) {
        this.enqueued = enqueued;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The idleSince
     */
    @JsonProperty("idleSince")
    public Date getIdleSince() {
        return idleSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param idleSince
     *     The idleSince
     */
    @JsonProperty("idleSince")
    public void setIdleSince(Date idleSince) {
        this.idleSince = idleSince;
    }

    /**
     * 
     * @return
     *     The steps
     */
    @JsonProperty("steps")
    public Integer getSteps() {
        return steps;
    }

    /**
     * 
     * @param steps
     *     The steps
     */
    @JsonProperty("steps")
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    /**
     * For order jobs only cause=order possible
     * 
     * @return
     *     The cause
     */
    @JsonProperty("cause")
    public RunningTask.Cause getCause() {
        return cause;
    }

    /**
     * For order jobs only cause=order possible
     * 
     * @param cause
     *     The cause
     */
    @JsonProperty("cause")
    public void setCause(RunningTask.Cause cause) {
        this.cause = cause;
    }

    /**
     * Only relevant for order jobs; cause=order resp.
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    public Order getOrder() {
        return order;
    }

    /**
     * Only relevant for order jobs; cause=order resp.
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    public void setOrder(Order order) {
        this.order = order;
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
        return new HashCodeBuilder().append(taskId).append(pid).append(startedAt).append(enqueued).append(idleSince).append(steps).append(cause).append(order).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RunningTask) == false) {
            return false;
        }
        RunningTask rhs = ((RunningTask) other);
        return new EqualsBuilder().append(taskId, rhs.taskId).append(pid, rhs.pid).append(startedAt, rhs.startedAt).append(enqueued, rhs.enqueued).append(idleSince, rhs.idleSince).append(steps, rhs.steps).append(cause, rhs.cause).append(order, rhs.order).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Cause {

        NONE("none"),
        MIN_TASKS("min_tasks"),
        PERIOD_ONCE("period_once"),
        PERIOD_SINGLE("period_single"),
        PERIOD_REPEAT("period_repeat"),
        QUEUE("queue"),
        QUEUE_AT("queue_at"),
        DIRECTORY("directory"),
        DELAY_AFTER_ERROR("delay_after_error"),
        ORDER("order");
        private final String value;
        private final static Map<String, RunningTask.Cause> CONSTANTS = new HashMap<String, RunningTask.Cause>();

        static {
            for (RunningTask.Cause c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Cause(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static RunningTask.Cause fromValue(String value) {
            RunningTask.Cause constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
