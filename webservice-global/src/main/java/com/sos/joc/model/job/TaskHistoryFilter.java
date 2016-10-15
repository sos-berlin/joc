
package com.sos.joc.model.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "job",
    "maxLastHistoryItems"
})
public class TaskHistoryFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
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
     * 
     */
    @JsonProperty("maxLastHistoryItems")
    private Integer maxLastHistoryItems;

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
     * 
     * @return
     *     The maxLastHistoryItems
     */
    @JsonProperty("maxLastHistoryItems")
    public Integer getMaxLastHistoryItems() {
        return maxLastHistoryItems;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param maxLastHistoryItems
     *     The maxLastHistoryItems
     */
    @JsonProperty("maxLastHistoryItems")
    public void setMaxLastHistoryItems(Integer maxLastHistoryItems) {
        this.maxLastHistoryItems = maxLastHistoryItems;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(job).append(maxLastHistoryItems).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskHistoryFilter) == false) {
            return false;
        }
        TaskHistoryFilter rhs = ((TaskHistoryFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(maxLastHistoryItems, rhs.maxLastHistoryItems).isEquals();
    }

}
