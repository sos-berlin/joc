
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * task modify
 * <p>
 * the command is part of the web servive url
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "jobs",
    "taskIds",
    "timeout"
})
public class ModifyTasksSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    @JsonProperty("jobs")
    private List<Job____> jobs = new ArrayList<Job____>();
    @JsonProperty("taskIds")
    private List<TaskId> taskIds = new ArrayList<TaskId>();
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("timeout")
    private Integer timeout;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
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
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public List<Job____> getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(List<Job____> jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * @return
     *     The taskIds
     */
    @JsonProperty("taskIds")
    public List<TaskId> getTaskIds() {
        return taskIds;
    }

    /**
     * 
     * @param taskIds
     *     The taskIds
     */
    @JsonProperty("taskIds")
    public void setTaskIds(List<TaskId> taskIds) {
        this.taskIds = taskIds;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The timeout
     */
    @JsonProperty("timeout")
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param timeout
     *     The timeout
     */
    @JsonProperty("timeout")
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
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
        return new HashCodeBuilder().append(jobschedulerId).append(jobs).append(taskIds).append(timeout).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ModifyTasksSchema) == false) {
            return false;
        }
        ModifyTasksSchema rhs = ((ModifyTasksSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(jobs, rhs.jobs).append(taskIds, rhs.taskIds).append(timeout, rhs.timeout).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
