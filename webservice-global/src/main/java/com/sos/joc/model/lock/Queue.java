
package com.sos.joc.model.lock;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "exclusive",
    "job"
})
public class Queue {

    /**
     * Is true iff the job want to use the lock exclusive
     * (Required)
     * 
     */
    @JsonProperty("exclusive")
    private Boolean exclusive;
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
     * Is true iff the job want to use the lock exclusive
     * (Required)
     * 
     * @return
     *     The exclusive
     */
    @JsonProperty("exclusive")
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
    @JsonProperty("exclusive")
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
