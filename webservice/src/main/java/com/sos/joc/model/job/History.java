
package com.sos.joc.model.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "job",
    "startTime",
    "endTime",
    "state",
    "taskId",
    "clusterMember",
    "steps",
    "exitCode",
    "error",
    "agent"
})
public class History {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("job")
    private String job;
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
    @JsonProperty("state")
    private State state;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("taskId")
    private Integer taskId;
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
    @JsonProperty("steps")
    private Integer steps;
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
    private ErrorSchema error;
    /**
     * agent url
     * 
     */
    @JsonProperty("agent")
    private String agent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
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
     * 
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
     *     The state
     */
    @JsonProperty("state")
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
    @JsonProperty("state")
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
    @JsonProperty("taskId")
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
    @JsonProperty("taskId")
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
    @JsonProperty("error")
    public void setError(ErrorSchema error) {
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(startTime).append(endTime).append(state).append(taskId).append(clusterMember).append(steps).append(exitCode).append(error).append(agent).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(job, rhs.job).append(startTime, rhs.startTime).append(endTime, rhs.endTime).append(state, rhs.state).append(taskId, rhs.taskId).append(clusterMember, rhs.clusterMember).append(steps, rhs.steps).append(exitCode, rhs.exitCode).append(error, rhs.error).append(agent, rhs.agent).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
