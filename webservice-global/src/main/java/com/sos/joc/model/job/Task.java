
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.order.OrderV;
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
    "taskId",
    "pid",
    "state",
    "plannedStart",
    "startedAt",
    "enqueued",
    "idleSince",
    "inProcessSince",
    "steps",
    "_cause",
    "order"
})
public class Task {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    private String taskId;
    @JsonProperty("pid")
    private Integer pid;
    /**
     * task state
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    private TaskState state;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("plannedStart")
    private Date plannedStart;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
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
    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     */
    @JsonProperty("_cause")
    private TaskCause _cause;
    /**
     * order (volatile part)
     * <p>
     * 
     * 
     */
    @JsonProperty("order")
    private OrderV order;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The taskId
     */
    @JsonProperty("taskId")
    public String getTaskId() {
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
    public void setTaskId(String taskId) {
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
     * task state
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public TaskState getState() {
        return state;
    }

    /**
     * task state
     * <p>
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(TaskState state) {
        this.state = state;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The plannedStart
     */
    @JsonProperty("plannedStart")
    public Date getPlannedStart() {
        return plannedStart;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param plannedStart
     *     The plannedStart
     */
    @JsonProperty("plannedStart")
    public void setPlannedStart(Date plannedStart) {
        this.plannedStart = plannedStart;
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
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     * @return
     *     The _cause
     */
    @JsonProperty("_cause")
    public TaskCause get_cause() {
        return _cause;
    }

    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     * @param _cause
     *     The _cause
     */
    @JsonProperty("_cause")
    public void set_cause(TaskCause _cause) {
        this._cause = _cause;
    }

    /**
     * order (volatile part)
     * <p>
     * 
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    public OrderV getOrder() {
        return order;
    }

    /**
     * order (volatile part)
     * <p>
     * 
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    public void setOrder(OrderV order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(taskId).append(pid).append(state).append(plannedStart).append(startedAt).append(enqueued).append(idleSince).append(inProcessSince).append(steps).append(_cause).append(order).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Task) == false) {
            return false;
        }
        Task rhs = ((Task) other);
        return new EqualsBuilder().append(taskId, rhs.taskId).append(pid, rhs.pid).append(state, rhs.state).append(plannedStart, rhs.plannedStart).append(startedAt, rhs.startedAt).append(enqueued, rhs.enqueued).append(idleSince, rhs.idleSince).append(inProcessSince, rhs.inProcessSince).append(steps, rhs.steps).append(_cause, rhs._cause).append(order, rhs.order).isEquals();
    }

}
