
package com.sos.joc.model.calendar;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * calendar
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "calendar"
})
public class Calendar200 {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * calendar
     * <p>
     * 
     * 
     */
    @JsonProperty("calendar")
    private Calendar calendar;

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * calendar
     * <p>
     * 
     * 
     * @return
     *     The calendar
     */
    @JsonProperty("calendar")
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * calendar
     * <p>
     * 
     * 
     * @param calendar
     *     The calendar
     */
    @JsonProperty("calendar")
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(calendar).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Calendar200) == false) {
            return false;
        }
        Calendar200 rhs = ((Calendar200) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(calendar, rhs.calendar).isEquals();
    }

}
