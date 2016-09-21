
package com.sos.joc.model.schedule;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * schedule with delivery date (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Schedule200VSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date deliveryDate;
    /**
     * schedule
     * <p>
     * 
     * 
     */
    private Schedule_ schedule;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * schedule
     * <p>
     * 
     * 
     * @return
     *     The schedule
     */
    public Schedule_ getSchedule() {
        return schedule;
    }

    /**
     * schedule
     * <p>
     * 
     * 
     * @param schedule
     *     The schedule
     */
    public void setSchedule(Schedule_ schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(schedule).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Schedule200VSchema) == false) {
            return false;
        }
        Schedule200VSchema rhs = ((Schedule200VSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(schedule, rhs.schedule).isEquals();
    }

}
