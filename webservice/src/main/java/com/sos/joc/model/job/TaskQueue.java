
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task object which is enqueued
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class TaskQueue {

    /**
     * 
     * (Required)
     * 
     */
    private Integer taskId;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date enqueued;

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
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
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
     * (Required)
     * 
     * @param enqueued
     *     The enqueued
     */
    public void setEnqueued(Date enqueued) {
        this.enqueued = enqueued;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(taskId).append(enqueued).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskQueue) == false) {
            return false;
        }
        TaskQueue rhs = ((TaskQueue) other);
        return new EqualsBuilder().append(taskId, rhs.taskId).append(enqueued, rhs.enqueued).isEquals();
    }

}
