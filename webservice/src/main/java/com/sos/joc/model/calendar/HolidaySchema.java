
package com.sos.joc.model.calendar;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * holiday
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class HolidaySchema {

    /**
     * 
     * (Required)
     * 
     */
    private List<Integer> weekdays = new ArrayList<Integer>();
    /**
     * 
     * (Required)
     * 
     */
    private List<String> dates = new ArrayList<String>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The weekdays
     */
    public List<Integer> getWeekdays() {
        return weekdays;
    }

    /**
     * 
     * (Required)
     * 
     * @param weekdays
     *     The weekdays
     */
    public void setWeekdays(List<Integer> weekdays) {
        this.weekdays = weekdays;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The dates
     */
    public List<String> getDates() {
        return dates;
    }

    /**
     * 
     * (Required)
     * 
     * @param dates
     *     The dates
     */
    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(weekdays).append(dates).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HolidaySchema) == false) {
            return false;
        }
        HolidaySchema rhs = ((HolidaySchema) other);
        return new EqualsBuilder().append(weekdays, rhs.weekdays).append(dates, rhs.dates).isEquals();
    }

}
