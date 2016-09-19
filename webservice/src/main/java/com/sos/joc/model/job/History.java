
package com.sos.joc.model.job;

import java.util.Date;
import javax.annotation.Generated;
import com.sos.joc.model.common.ErrorSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task in history collection
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class History {

    /**
     * 
     * (Required)
     * 
     */
    private String job;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date startTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    private Date endTime;
    /**
     * 
     * (Required)
     * 
     */
    private State state;
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
     * 
     */
    private String clusterMember;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer steps;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer exitCode;
    /**
     * error
     * <p>
     * 
     * 
     */
    private ErrorSchema error;
    /**
     * agent url
     * 
     */
    private String agent;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The job
     */
    public String getJob() {
        return job;
    }

    /**
     * 
     * (Required)
     * 
     * @param job
     *     The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param startTime
     *     The startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param endTime
     *     The endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    public void setState(State state) {
        this.state = state;
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
     * 
     * @return
     *     The clusterMember
     */
    public String getClusterMember() {
        return clusterMember;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param clusterMember
     *     The clusterMember
     */
    public void setClusterMember(String clusterMember) {
        this.clusterMember = clusterMember;
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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The exitCode
     */
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param exitCode
     *     The exitCode
     */
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @return
     *     The error
     */
    public ErrorSchema getError() {
        return error;
    }

    /**
     * error
     * <p>
     * 
     * 
     * @param error
     *     The error
     */
    public void setError(ErrorSchema error) {
        this.error = error;
    }

    /**
     * agent url
     * 
     * @return
     *     The agent
     */
    public String getAgent() {
        return agent;
    }

    /**
     * agent url
     * 
     * @param agent
     *     The agent
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(startTime).append(endTime).append(state).append(taskId).append(clusterMember).append(steps).append(exitCode).append(error).append(agent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof History) == false) {
            return false;
        }
        History rhs = ((History) other);
        return new EqualsBuilder().append(job, rhs.job).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(state, rhs.state).append(taskId, rhs.taskId).append(clusterMember, rhs.clusterMember).append(steps, rhs.steps).append(exitCode, rhs.exitCode).append(error, rhs.error).append(agent, rhs.agent).isEquals();
    }

}
