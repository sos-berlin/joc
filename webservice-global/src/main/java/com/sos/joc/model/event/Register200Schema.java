
package com.sos.joc.model.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * collect events with delivery date
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Register200Schema {

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * register, unregister or collect event
     * <p>
     * 
     * (Required)
     * 
     */
    private List<java.lang.Object> events = new ArrayList<java.lang.Object>();

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * register, unregister or collect event
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The events
     */
    public List<java.lang.Object> getEvents() {
        return events;
    }

    /**
     * register, unregister or collect event
     * <p>
     * 
     * (Required)
     * 
     * @param events
     *     The events
     */
    public void setEvents(List<java.lang.Object> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(events).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Register200Schema) == false) {
            return false;
        }
        Register200Schema rhs = ((Register200Schema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(events, rhs.events).isEquals();
    }

}
