
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
    "_cause",
    "order"
})
public class RunningTask {

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
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * 
     */
    @JsonProperty("_cause")
    private TaskCause _cause;
    /**
     * order in task
     * <p>
     * Only relevant for order jobs; cause=order resp.
     * 
     */
    @JsonProperty("order")
    private OrderOfTask order;

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
     * order in task
     * <p>
     * Only relevant for order jobs; cause=order resp.
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    public OrderOfTask getOrder() {
        return order;
    }

    /**
     * order in task
     * <p>
     * Only relevant for order jobs; cause=order resp.
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    public void setOrder(OrderOfTask order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(taskId).append(pid).append(startedAt).append(enqueued).append(idleSince).append(steps).append(_cause).append(order).toHashCode();
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
        return new EqualsBuilder().append(taskId, rhs.taskId).append(pid, rhs.pid).append(startedAt, rhs.startedAt).append(enqueued, rhs.enqueued).append(idleSince, rhs.idleSince).append(steps, rhs.steps).append(_cause, rhs._cause).append(order, rhs.order).isEquals();
    }

}
