
package com.sos.joc.model.order;

import java.util.Date;
import javax.annotation.Generated;
import com.sos.joc.model.common.ErrorSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * item of step history collection of one order run
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Step {

    /**
     * 
     * (Required)
     * 
     */
    private String node;
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
    private Integer step;
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
    private Integer clusterMember;
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
     *     The node
     */
    public String getNode() {
        return node;
    }

    /**
     * 
     * (Required)
     * 
     * @param node
     *     The node
     */
    public void setNode(String node) {
        this.node = node;
    }

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
     *     The step
     */
    public Integer getStep() {
        return step;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param step
     *     The step
     */
    public void setStep(Integer step) {
        this.step = step;
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
    public Integer getClusterMember() {
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
    public void setClusterMember(Integer clusterMember) {
        this.clusterMember = clusterMember;
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
        return new HashCodeBuilder().append(node).append(job).append(step).append(startTime).append(endTime).append(taskId).append(clusterMember).append(exitCode).append(error).append(agent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Step) == false) {
            return false;
        }
        Step rhs = ((Step) other);
        return new EqualsBuilder().append(node, rhs.node).append(job, rhs.job).append(step, rhs.step).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(taskId, rhs.taskId).append(clusterMember, rhs.clusterMember).append(exitCode, rhs.exitCode).append(error, rhs.error).append(agent, rhs.agent).isEquals();
    }

}
