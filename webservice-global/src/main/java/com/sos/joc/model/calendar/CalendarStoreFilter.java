
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * calendarFilter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "calendar"
})
public class CalendarStoreFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * calendar
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("calendar")
    private Calendar calendar;

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
     * calendar
     * <p>
     * 
     * (Required)
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
     * (Required)
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
        return new HashCodeBuilder().append(jobschedulerId).append(calendar).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CalendarStoreFilter) == false) {
            return false;
        }
        CalendarStoreFilter rhs = ((CalendarStoreFilter) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(calendar, rhs.calendar).isEquals();
    }

}
