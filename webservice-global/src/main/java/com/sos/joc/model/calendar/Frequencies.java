
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
 * frequencies
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "mode",
    "dates",
    "weekdays",
    "ultimos",
    "monthdays",
    "months",
    "holidays",
    "intervals"
})
public class Frequencies {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mode")
    private FrequencyMode mode;
    @JsonProperty("dates")
    private List<String> dates = new ArrayList<String>();
    /**
     * weekdays
     * <p>
     * 
     * 
     */
    @JsonProperty("weekdays")
    private Weekdays weekdays;
    @JsonProperty("ultimos")
    private List<Ultimo> ultimos = new ArrayList<Ultimo>();
    /**
     * monthdays
     * <p>
     * 
     * 
     */
    @JsonProperty("monthdays")
    private Monthdays monthdays;
    @JsonProperty("months")
    private List<Month> months = new ArrayList<Month>();
    @JsonProperty("holidays")
    private List<Holiday> holidays = new ArrayList<Holiday>();
    @JsonProperty("intervals")
    private List<Every> intervals = new ArrayList<Every>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The mode
     */
    @JsonProperty("mode")
    public FrequencyMode getMode() {
        return mode;
    }

    /**
     * 
     * (Required)
     * 
     * @param mode
     *     The mode
     */
    @JsonProperty("mode")
    public void setMode(FrequencyMode mode) {
        this.mode = mode;
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
     * weekdays
     * <p>
     * 
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public Weekdays getWeekdays() {
        return weekdays;
    }

    /**
     * weekdays
     * <p>
     * 
     * 
     * @param weekdays
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public void setWeekdays(Weekdays weekdays) {
        this.weekdays = weekdays;
    }

    /**
     * 
     * @return
     *     The ultimos
     */
    @JsonProperty("ultimos")
    public List<Ultimo> getUltimos() {
        return ultimos;
    }

    /**
     * 
     * @param ultimos
     *     The ultimos
     */
    @JsonProperty("ultimos")
    public void setUltimos(List<Ultimo> ultimos) {
        this.ultimos = ultimos;
    }

    /**
     * monthdays
     * <p>
     * 
     * 
     * @return
     *     The monthdays
     */
    @JsonProperty("monthdays")
    public Monthdays getMonthdays() {
        return monthdays;
    }

    /**
     * monthdays
     * <p>
     * 
     * 
     * @param monthdays
     *     The monthdays
     */
    @JsonProperty("monthdays")
    public void setMonthdays(Monthdays monthdays) {
        this.monthdays = monthdays;
    }

    /**
     * 
     * @return
     *     The months
     */
    @JsonProperty("months")
    public List<Month> getMonths() {
        return months;
    }

    /**
     * 
     * @param months
     *     The months
     */
    @JsonProperty("months")
    public void setMonths(List<Month> months) {
        this.months = months;
    }

    /**
     * 
     * @return
     *     The holidays
     */
    @JsonProperty("holidays")
    public List<Holiday> getHolidays() {
        return holidays;
    }

    /**
     * 
     * @param holidays
     *     The holidays
     */
    @JsonProperty("holidays")
    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    /**
     * 
     * @return
     *     The intervals
     */
    @JsonProperty("intervals")
    public List<Every> getIntervals() {
        return intervals;
    }

    /**
     * 
     * @param intervals
     *     The intervals
     */
    @JsonProperty("intervals")
    public void setIntervals(List<Every> intervals) {
        this.intervals = intervals;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(mode).append(dates).append(weekdays).append(ultimos).append(monthdays).append(months).append(holidays).append(intervals).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Frequencies) == false) {
            return false;
        }
        Frequencies rhs = ((Frequencies) other);
        return new EqualsBuilder().append(mode, rhs.mode).append(dates, rhs.dates).append(weekdays, rhs.weekdays).append(ultimos, rhs.ultimos).append(monthdays, rhs.monthdays).append(months, rhs.months).append(holidays, rhs.holidays).append(intervals, rhs.intervals).isEquals();
    }

}
