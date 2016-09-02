
package com.sos.joc.model.job;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class TaskId {

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer taskId;

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The taskId
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param taskId
     *     The taskId
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(taskId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskId) == false) {
            return false;
        }
        TaskId rhs = ((TaskId) other);
        return new EqualsBuilder().append(taskId, rhs.taskId).isEquals();
    }

}
