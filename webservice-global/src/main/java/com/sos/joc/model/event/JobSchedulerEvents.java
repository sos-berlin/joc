
package com.sos.joc.model.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * events
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "events",
    "deliveryDate"
})
public class JobSchedulerEvents {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("events")
    private List<JobSchedulerEvent> events = new ArrayList<JobSchedulerEvent>();
    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The events
     */
    @JsonProperty("events")
    public List<JobSchedulerEvent> getEvents() {
        return events;
    }

    /**
     * 
     * (Required)
     * 
     * @param events
     *     The events
     */
    @JsonProperty("events")
    public void setEvents(List<JobSchedulerEvent> events) {
        this.events = events;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(events).append(deliveryDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobSchedulerEvents) == false) {
            return false;
        }
        JobSchedulerEvents rhs = ((JobSchedulerEvents) other);
        return new EqualsBuilder().append(events, rhs.events).append(deliveryDate, rhs.deliveryDate).isEquals();
    }

}
