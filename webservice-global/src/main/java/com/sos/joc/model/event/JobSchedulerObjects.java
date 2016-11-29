
package com.sos.joc.model.event;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobScheduler objects filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "eventId",
    "objects"
})
public class JobSchedulerObjects {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("eventId")
    private String eventId;
    /**
     * collection of JobScheduler object with path and type
     * 
     */
    @JsonProperty("objects")
    private List<JobSchedulerObject> objects = new ArrayList<JobSchedulerObject>();

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
     * (Required)
     * 
     * @return
     *     The eventId
     */
    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
    }

    /**
     * 
     * (Required)
     * 
     * @param eventId
     *     The eventId
     */
    @JsonProperty("eventId")
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * collection of JobScheduler object with path and type
     * 
     * @return
     *     The objects
     */
    @JsonProperty("objects")
    public List<JobSchedulerObject> getObjects() {
        return objects;
    }

    /**
     * collection of JobScheduler object with path and type
     * 
     * @param objects
     *     The objects
     */
    @JsonProperty("objects")
    public void setObjects(List<JobSchedulerObject> objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(eventId).append(objects).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobSchedulerObjects) == false) {
            return false;
        }
        JobSchedulerObjects rhs = ((JobSchedulerObjects) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(eventId, rhs.eventId).append(objects, rhs.objects).isEquals();
    }

}
