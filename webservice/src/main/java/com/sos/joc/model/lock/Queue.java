
package com.sos.joc.model.lock;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Queue {

    /**
     * Is true iff the job want to use the lock exclusive
     * (Required)
     * 
     */
    private Boolean exclusive;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String job;

    /**
     * Is true iff the job want to use the lock exclusive
     * (Required)
     * 
     * @return
     *     The exclusive
     */
    public Boolean getExclusive() {
        return exclusive;
    }

    /**
     * Is true iff the job want to use the lock exclusive
     * (Required)
     * 
     * @param exclusive
     *     The exclusive
     */
    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(exclusive).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Queue) == false) {
            return false;
        }
        Queue rhs = ((Queue) other);
        return new EqualsBuilder().append(exclusive, rhs.exclusive).append(job, rhs.job).isEquals();
    }

}
