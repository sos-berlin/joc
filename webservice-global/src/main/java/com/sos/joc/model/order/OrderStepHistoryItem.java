
package com.sos.joc.model.order;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Err;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * item of step history collection of one order run
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "node",
    "job",
    "step",
    "startTime",
    "endTime",
    "taskId",
    "clusterMember",
    "exitCode",
    "error",
    "agent"
})
public class OrderStepHistoryItem {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("node")
    private String node;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("job")
    private String job;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("step")
    private Integer step;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("startTime")
    private Date startTime;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("endTime")
    private Date endTime;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    private String taskId;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("clusterMember")
    private Integer clusterMember;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("exitCode")
    private Integer exitCode;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    /**
     * agent url
     * 
     */
    @JsonProperty("agent")
    private String agent;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The node
     */
    @JsonProperty("node")
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
    @JsonProperty("node")
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
    @JsonProperty("job")
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
    @JsonProperty("job")
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
    @JsonProperty("step")
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
    @JsonProperty("step")
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
    @JsonProperty("startTime")
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
    @JsonProperty("startTime")
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
    @JsonProperty("endTime")
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
    @JsonProperty("endTime")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

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
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The clusterMember
     */
    @JsonProperty("clusterMember")
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
    @JsonProperty("clusterMember")
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
    @JsonProperty("exitCode")
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
    @JsonProperty("exitCode")
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
    @JsonProperty("error")
    public Err getError() {
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
    @JsonProperty("error")
    public void setError(Err error) {
        this.error = error;
    }

    /**
     * agent url
     * 
     * @return
     *     The agent
     */
    @JsonProperty("agent")
    public String getAgent() {
        return agent;
    }

    /**
     * agent url
     * 
     * @param agent
     *     The agent
     */
    @JsonProperty("agent")
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
        if ((other instanceof OrderStepHistoryItem) == false) {
            return false;
        }
        OrderStepHistoryItem rhs = ((OrderStepHistoryItem) other);
        return new EqualsBuilder().append(node, rhs.node).append(job, rhs.job).append(step, rhs.step).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(taskId, rhs.taskId).append(clusterMember, rhs.clusterMember).append(exitCode, rhs.exitCode).append(error, rhs.error).append(agent, rhs.agent).isEquals();
    }

}
