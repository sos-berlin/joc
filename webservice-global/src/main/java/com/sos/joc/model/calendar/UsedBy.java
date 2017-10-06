
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "objectType",
    "path"
})
public class UsedBy {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    @JsonProperty("objectType")
    private JobSchedulerObjectType objectType;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;

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
     * JobScheduler object type
     * <p>
     * 
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    public JobSchedulerObjectType getObjectType() {
        return objectType;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    public void setObjectType(JobSchedulerObjectType objectType) {
        this.objectType = objectType;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(objectType).append(path).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UsedBy) == false) {
            return false;
        }
        UsedBy rhs = ((UsedBy) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(objectType, rhs.objectType).append(path, rhs.path).isEquals();
    }

}
