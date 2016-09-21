
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Job___ {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    private String job;
    /**
     * Field to comment manually job modifications which can be logged.
     * 
     */
    private String comment;
    private List<TaskId> taskIds = new ArrayList<TaskId>();

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
     * Field to comment manually job modifications which can be logged.
     * 
     * @return
     *     The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Field to comment manually job modifications which can be logged.
     * 
     * @param comment
     *     The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 
     * @return
     *     The taskIds
     */
    public List<TaskId> getTaskIds() {
        return taskIds;
    }

    /**
     * 
     * @param taskIds
     *     The taskIds
     */
    public void setTaskIds(List<TaskId> taskIds) {
        this.taskIds = taskIds;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(comment).append(taskIds).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job___) == false) {
            return false;
        }
        Job___ rhs = ((Job___) other);
        return new EqualsBuilder().append(job, rhs.job).append(comment, rhs.comment).append(taskIds, rhs.taskIds).isEquals();
    }

}
