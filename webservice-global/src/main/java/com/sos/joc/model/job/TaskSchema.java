
package com.sos.joc.model.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class TaskSchema {

    /**
     * 
     * (Required)
     * 
     */
    private Integer id;
    private Integer pid;
    /**
     * 
     * (Required)
     * 
     */
    private State___ state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date startAt;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date runningSince;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date enqueued;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date idleSince;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date inProcessSince;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer steps;
    private TaskSchema.Cause cause;
    /**
     * 
     */
    private Order_ order;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The id
     */
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
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 
     * @param pid
     *     The pid
     */
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The steps
     */
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
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    /**
     * 
     * @return
     *     The cause
     */
    public TaskSchema.Cause getCause() {
        return cause;
    }

    /**
     * 
     * @param cause
     *     The _cause
     */
    public void setCause(TaskSchema.Cause cause) {
        this.cause = cause;
    }

    /**
     * 
     * @return
     *     The order
     */
    public Order_ getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    public void setOrder(Order_ order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(pid).append(state).append(startAt).append(runningSince).append(enqueued).append(idleSince).append(inProcessSince).append(steps).append(cause).append(order).toHashCode();
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
        return new EqualsBuilder().append(id, rhs.id).append(pid, rhs.pid).append(state, rhs.state).append(startAt, rhs.startAt).append(runningSince, rhs.runningSince).append(enqueued, rhs.enqueued).append(idleSince, rhs.idleSince).append(inProcessSince, rhs.inProcessSince).append(steps, rhs.steps).append(cause, rhs.cause).append(order, rhs.order).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Cause {

        PERIOD_ONCE("PERIOD_ONCE"),
        PERIOD_SINGLE("PERIOD_SINGLE"),
        PERIOD_REPEAT("PERIOD_REPEAT"),
        QUEUE("QUEUE"),
        QUEUE_AT("QUEUE_AT"),
        DIRECTORY("DIRECTORY"),
        DELAY_AFTER_ERROR("DELAY_AFTER_ERROR"),
        ORDER("ORDER");
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

        @Override
        public String toString() {
            return this.value;
        }

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
