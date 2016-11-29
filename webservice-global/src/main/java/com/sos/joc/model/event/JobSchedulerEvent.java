
package com.sos.joc.model.event;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.Err;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "error",
    "eventId",
    "eventSnapshots"
})
public class JobSchedulerEvent {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * error
     * <p>
     * 
     * 
     */
    @JsonProperty("error")
    private Err error;
    @JsonProperty("eventId")
    private String eventId;
    @JsonProperty("eventSnapshots")
    private List<EventSnapshot> eventSnapshots = new ArrayList<EventSnapshot>();

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
     * error
     * <p>
     * 
     * 
     * @return
     *     The error
     */
    @JsonProperty("error")
    public Err getError() {
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
    public void setError(Err error) {
        this.error = error;
    }

    /**
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
     * @param eventId
     *     The eventId
     */
    @JsonProperty("eventId")
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * 
     * @return
     *     The eventSnapshots
     */
    @JsonProperty("eventSnapshots")
    public List<EventSnapshot> getEventSnapshots() {
        return eventSnapshots;
    }

    /**
     * 
     * @param eventSnapshots
     *     The eventSnapshots
     */
    @JsonProperty("eventSnapshots")
    public void setEventSnapshots(List<EventSnapshot> eventSnapshots) {
        this.eventSnapshots = eventSnapshots;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(error).append(eventId).append(eventSnapshots).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobSchedulerEvent) == false) {
            return false;
        }
        JobSchedulerEvent rhs = ((JobSchedulerEvent) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(error, rhs.error).append(eventId, rhs.eventId).append(eventSnapshots, rhs.eventSnapshots).isEquals();
    }

}
