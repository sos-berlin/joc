
package com.sos.joc.model.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * running task
 * <p>
 * task object of an order job which is running
 * 
 */
@Generated("org.jsonschema2pojo")
public class RunningTask {

    /**
     * 
     * (Required)
     * 
     */
    private Integer taskId;
    private Integer pid;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date startedAt;
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
    private Integer steps;
    /**
     * For order jobs only cause=order possible
     * 
     */
    private RunningTask.Cause cause;
    /**
     * Only relevant for order jobs; cause=order resp.
     * 
     */
    private Order order;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The taskId
     */
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
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
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
     * (Required)
     * 
     * @param startedAt
     *     The startedAt
     */
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
     * 
     * @return
     *     The steps
     */
    public Integer getSteps() {
        return steps;
    }

    /**
     * 
     * @param steps
     *     The steps
     */
    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    /**
     * For order jobs only cause=order possible
     * 
     * @return
     *     The cause
     */
    public RunningTask.Cause getCause() {
        return cause;
    }

    /**
     * For order jobs only cause=order possible
     * 
     * @param cause
     *     The _cause
     */
    public void setCause(RunningTask.Cause cause) {
        this.cause = cause;
    }

    /**
     * Only relevant for order jobs; cause=order resp.
     * 
     * @return
     *     The order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Only relevant for order jobs; cause=order resp.
     * 
     * @param order
     *     The order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(taskId).append(pid).append(startedAt).append(enqueued).append(idleSince).append(steps).append(cause).append(order).toHashCode();
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
        return new EqualsBuilder().append(taskId, rhs.taskId).append(pid, rhs.pid).append(startedAt, rhs.startedAt).append(enqueued, rhs.enqueued).append(idleSince, rhs.idleSince).append(steps, rhs.steps).append(cause, rhs.cause).append(order, rhs.order).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Cause {

        NONE("NONE"),
        MIN_TASKS("MIN_TASKS"),
        PERIOD_ONCE("PERIOD_ONCE"),
        PERIOD_SINGLE("PERIOD_SINGLE"),
        PERIOD_REPEAT("PERIOD_REPEAT"),
        QUEUE("QUEUE"),
        QUEUE_AT("QUEUE_AT"),
        DIRECTORY("DIRECTORY"),
        DELAY_AFTER_ERROR("DELAY_AFTER_ERROR"),
        ORDER("ORDER");
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

        @Override
        public String toString() {
            return this.value;
        }

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
