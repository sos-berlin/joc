
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
 * task
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "pid",
    "state",
    "startAt",
    "runningSince",
    "enqueued",
    "idleSince",
    "inProcessSince",
    "steps",
    "cause",
    "order"
})
public class TaskSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("pid")
    private Integer pid;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    private State___ state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("startAt")
    private Date startAt;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("runningSince")
    private Date runningSince;
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
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("inProcessSince")
    private Date inProcessSince;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("steps")
    private Integer steps;
    @JsonProperty("cause")
    private TaskSchema.Cause cause;
    /**
     * 
     */
    @JsonProperty("order")
    private Order_ order;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * (Required)
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
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
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public State___ getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(State___ state) {
        this.state = state;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The startAt
     */
    @JsonProperty("startAt")
    public Date getStartAt() {
        return startAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param startAt
     *     The startAt
     */
    @JsonProperty("startAt")
    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The runningSince
     */
    @JsonProperty("runningSince")
    public Date getRunningSince() {
        return runningSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param runningSince
     *     The runningSince
     */
    @JsonProperty("runningSince")
    public void setRunningSince(Date runningSince) {
        this.runningSince = runningSince;
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The steps
     */
    @JsonProperty("steps")
    public Integer getSteps() {
        return steps;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param steps
     *     The steps
     */
    @JsonProperty("steps")
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    /**
     * 
     * @return
     *     The cause
     */
    @JsonProperty("cause")
    public TaskSchema.Cause getCause() {
        return cause;
    }

    /**
     * 
     * @param cause
     *     The cause
     */
    @JsonProperty("cause")
    public void setCause(TaskSchema.Cause cause) {
        this.cause = cause;
    }

    /**
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    public Order_ getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    public void setOrder(Order_ order) {
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
        return new HashCodeBuilder().append(id).append(pid).append(state).append(startAt).append(runningSince).append(enqueued).append(idleSince).append(inProcessSince).append(steps).append(cause).append(order).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskSchema) == false) {
            return false;
        }
        TaskSchema rhs = ((TaskSchema) other);
        return new EqualsBuilder().append(id, rhs.id).append(pid, rhs.pid).append(state, rhs.state).append(startAt, rhs.startAt).append(runningSince, rhs.runningSince).append(enqueued, rhs.enqueued).append(idleSince, rhs.idleSince).append(inProcessSince, rhs.inProcessSince).append(steps, rhs.steps).append(cause, rhs.cause).append(order, rhs.order).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Cause {

        period_once("period_once"),
        period_single("period_single"),
        period_repeat("period_repeat"),
        queue("queue"),
        queue_at("queue_at"),
        directory("directory"),
        delay_after_error("delay_after_error"),
        order("order");
        private final String value;
        private final static Map<String, TaskSchema.Cause> CONSTANTS = new HashMap<String, TaskSchema.Cause>();

        static {
            for (TaskSchema.Cause c: values()) {
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
        public static TaskSchema.Cause fromValue(String value) {
            TaskSchema.Cause constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
