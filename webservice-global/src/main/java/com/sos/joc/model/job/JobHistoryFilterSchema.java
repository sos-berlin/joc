
package com.sos.joc.model.job;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobFilter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class JobHistoryFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
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
     * 
     */
    private Integer maxLastHistoryItems;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
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
     * 
     * @return
     *     The maxLastHistoryItems
     */
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
        if ((other instanceof JobHistoryFilterSchema) == false) {
            return false;
        }
        JobHistoryFilterSchema rhs = ((JobHistoryFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(job, rhs.job).append(maxLastHistoryItems, rhs.maxLastHistoryItems).isEquals();
    }

}
