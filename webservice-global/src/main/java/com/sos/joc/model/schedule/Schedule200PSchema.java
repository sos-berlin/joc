
package com.sos.joc.model.schedule;

import java.util.Date;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * schedule with delivery date (permanent part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Schedule200PSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    private Date deliveryDate;
    /**
     * schedule (permant part)
     * <p>
     * 
     * 
     */
    private Schedule schedule;

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
     * schedule (permant part)
     * <p>
     * 
     * 
     * @return
     *     The schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * schedule (permant part)
     * <p>
     * 
     * 
     * @param schedule
     *     The schedule
     */
    public void setSchedule(Schedule schedule) {
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
        if ((other instanceof Schedule200PSchema) == false) {
            return false;
        }
        Schedule200PSchema rhs = ((Schedule200PSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(schedule, rhs.schedule).isEquals();
    }

}
