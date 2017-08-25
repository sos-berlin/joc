
package com.sos.joc.model.calendar;

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
 * holiday
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "calendarName",
    "dates",
    "weekdays"
})
public class Holiday {

    @JsonProperty("calendarName")
    private String calendarName;
    @JsonProperty("dates")
    private List<String> dates = new ArrayList<String>();
    @JsonProperty("weekdays")
    private List<String> weekdays = new ArrayList<String>();

    /**
     * 
     * @return
     *     The calendarName
     */
    @JsonProperty("calendarName")
    public String getCalendarName() {
        return calendarName;
    }

    /**
     * 
     * @param calendarName
     *     The calendarName
     */
    @JsonProperty("calendarName")
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    /**
     * 
     * @return
     *     The dates
     */
    @JsonProperty("dates")
    public List<String> getDates() {
        return dates;
    }

    /**
     * 
     * @param dates
     *     The dates
     */
    @JsonProperty("dates")
    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    /**
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public List<String> getWeekdays() {
        return weekdays;
    }

    /**
     * 
     * @param weekdays
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public void setWeekdays(List<String> weekdays) {
        this.weekdays = weekdays;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendarName).append(dates).append(weekdays).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Holiday) == false) {
            return false;
        }
        Holiday rhs = ((Holiday) other);
        return new EqualsBuilder().append(calendarName, rhs.calendarName).append(dates, rhs.dates).append(weekdays, rhs.weekdays).isEquals();
    }

}
