
package com.sos.joc.model.report;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.job.TaskCause;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "agent",
    "cause",
    "numOfSuccessfulTasks"
})
public class Agent {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * Url of an Agent
     * (Required)
     * 
     */
    @JsonProperty("agent")
    private String agent;
    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * (Required)
     * 
     */
    @JsonProperty("cause")
    private TaskCause cause;
    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("numOfSuccessfulTasks")
    private Long numOfSuccessfulTasks;

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * Url of an Agent
     * (Required)
     * 
     * @return
     *     The agent
     */
    @JsonProperty("agent")
    public String getAgent() {
        return agent;
    }

    /**
     * Url of an Agent
     * (Required)
     * 
     * @param agent
     *     The agent
     */
    @JsonProperty("agent")
    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * (Required)
     * 
     * @return
     *     The cause
     */
    @JsonProperty("cause")
    public TaskCause getCause() {
        return cause;
    }

    /**
     * task cause
     * <p>
     * For order jobs only cause=order possible
     * (Required)
     * 
     * @param cause
     *     The cause
     */
    @JsonProperty("cause")
    public void setCause(TaskCause cause) {
        this.cause = cause;
    }

    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The numOfSuccessfulTasks
     */
    @JsonProperty("numOfSuccessfulTasks")
    public Long getNumOfSuccessfulTasks() {
        return numOfSuccessfulTasks;
    }

    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     * @param numOfSuccessfulTasks
     *     The numOfSuccessfulTasks
     */
    @JsonProperty("numOfSuccessfulTasks")
    public void setNumOfSuccessfulTasks(Long numOfSuccessfulTasks) {
        this.numOfSuccessfulTasks = numOfSuccessfulTasks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(agent).append(cause).append(numOfSuccessfulTasks).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Agent) == false) {
            return false;
        }
        Agent rhs = ((Agent) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(agent, rhs.agent).append(cause, rhs.cause).append(numOfSuccessfulTasks, rhs.numOfSuccessfulTasks).isEquals();
    }

}
