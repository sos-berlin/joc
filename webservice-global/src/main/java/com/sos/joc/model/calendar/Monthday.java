
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
 * monthday
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "days",
    "weekdays"
})
public class Monthday {

    @JsonProperty("days")
    private List<String> days = new ArrayList<String>();
    @JsonProperty("weekdays")
    private List<WhichDay> weekdays = new ArrayList<WhichDay>();

    /**
     * 
     * @return
     *     The days
     */
    @JsonProperty("days")
    public List<String> getDays() {
        return days;
    }

    /**
     * 
     * @param days
     *     The days
     */
    @JsonProperty("days")
    public void setDays(List<String> days) {
        this.days = days;
    }

    /**
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public List<WhichDay> getWeekdays() {
        return weekdays;
    }

    /**
     * 
     * @param weekdays
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public void setWeekdays(List<WhichDay> weekdays) {
        this.weekdays = weekdays;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(days).append(weekdays).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Monthday) == false) {
            return false;
        }
        Monthday rhs = ((Monthday) other);
        return new EqualsBuilder().append(days, rhs.days).append(weekdays, rhs.weekdays).isEquals();
    }

}
