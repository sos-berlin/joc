
package com.sos.joc.model.processClass;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * process
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ProcessSchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String job;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer taskId;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer pid;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date runningSince;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
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
     * (Required)
     * 
     * @param job
     *     The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * non negative integer
     * <p>
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
     * non negative integer
     * <p>
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
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
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
     *     The runningSince
     */
    public Date getRunningSince() {
        return runningSince;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param runningSince
     *     The runningSince
     */
    public void setRunningSince(Date runningSince) {
        this.runningSince = runningSince;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(taskId).append(pid).append(runningSince).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessSchema) == false) {
            return false;
        }
        ProcessSchema rhs = ((ProcessSchema) other);
        return new EqualsBuilder().append(job, rhs.job).append(taskId, rhs.taskId).append(pid, rhs.pid).append(runningSince, rhs.runningSince).isEquals();
    }

}
