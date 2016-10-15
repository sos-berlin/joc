
package com.sos.joc.model.processClass;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * process
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "job",
    "taskId",
    "pid",
    "runningSince",
    "agent"
})
public class Process {

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
     * (Required)
     * 
     */
    @JsonProperty("pid")
    private Integer pid;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    @JsonProperty("runningSince")
    private Date runningSince;
    /**
     * url
     * 
     */
    @JsonProperty("agent")
    private String agent;

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
     * (Required)
     * 
     * @return
     *     The pid
     */
    @JsonProperty("pid")
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
     *     The runningSince
     */
    @JsonProperty("runningSince")
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
    @JsonProperty("runningSince")
    public void setRunningSince(Date runningSince) {
        this.runningSince = runningSince;
    }

    /**
     * url
     * 
     * @return
     *     The agent
     */
    @JsonProperty("agent")
    public String getAgent() {
        return agent;
    }

    /**
     * url
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
        return new HashCodeBuilder().append(job).append(taskId).append(pid).append(runningSince).append(agent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Process) == false) {
            return false;
        }
        Process rhs = ((Process) other);
        return new EqualsBuilder().append(job, rhs.job).append(taskId, rhs.taskId).append(pid, rhs.pid).append(runningSince, rhs.runningSince).append(agent, rhs.agent).isEquals();
    }

}
