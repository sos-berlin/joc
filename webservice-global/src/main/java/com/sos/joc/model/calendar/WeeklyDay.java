
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "day",
    "weekOfMonth"
})
public class WeeklyDay {

    /**
     * dayOfWeek number
     * <p>
     * digit from 0-6, 0=Sunday, 1=Monday, ..., 6=Saturday
     * 
     */
    @JsonProperty("day")
    private Integer day;
    @JsonProperty("weekOfMonth")
    private Integer weekOfMonth;

    /**
     * dayOfWeek number
     * <p>
     * digit from 0-6, 0=Sunday, 1=Monday, ..., 6=Saturday
     * 
     * @return
     *     The day
     */
    @JsonProperty("day")
    public Integer getDay() {
        return day;
    }

    /**
     * dayOfWeek number
     * <p>
     * digit from 0-6, 0=Sunday, 1=Monday, ..., 6=Saturday
     * 
     * @param day
     *     The day
     */
    @JsonProperty("day")
    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * 
     * @return
     *     The weekOfMonth
     */
    @JsonProperty("weekOfMonth")
    public Integer getWeekOfMonth() {
        return weekOfMonth;
    }

    /**
     * 
     * @param weekOfMonth
     *     The weekOfMonth
     */
    @JsonProperty("weekOfMonth")
    public void setWeekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(day).append(weekOfMonth).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WeeklyDay) == false) {
            return false;
        }
        WeeklyDay rhs = ((WeeklyDay) other);
        return new EqualsBuilder().append(day, rhs.day).append(weekOfMonth, rhs.weekOfMonth).isEquals();
    }

}
