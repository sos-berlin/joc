
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
    "monthdays",
    "ultimos",
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
    @JsonProperty("weekdays")
    private List<WeekDays> weekdays = new ArrayList<WeekDays>();
    @JsonProperty("monthdays")
    private List<MonthDays> monthdays = new ArrayList<MonthDays>();
    @JsonProperty("ultimos")
    private List<MonthDays> ultimos = new ArrayList<MonthDays>();
    @JsonProperty("months")
    private List<Months> months = new ArrayList<Months>();
    @JsonProperty("holidays")
    private List<Holidays> holidays = new ArrayList<Holidays>();
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
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public List<WeekDays> getWeekdays() {
        return weekdays;
    }

    /**
     * 
     * @param weekdays
     *     The weekdays
     */
    @JsonProperty("weekdays")
    public void setWeekdays(List<WeekDays> weekdays) {
        this.weekdays = weekdays;
    }

    /**
     * 
     * @return
     *     The monthdays
     */
    @JsonProperty("monthdays")
    public List<MonthDays> getMonthdays() {
        return monthdays;
    }

    /**
     * 
     * @param monthdays
     *     The monthdays
     */
    @JsonProperty("monthdays")
    public void setMonthdays(List<MonthDays> monthdays) {
        this.monthdays = monthdays;
    }

    /**
     * 
     * @return
     *     The ultimos
     */
    @JsonProperty("ultimos")
    public List<MonthDays> getUltimos() {
        return ultimos;
    }

    /**
     * 
     * @param ultimos
     *     The ultimos
     */
    @JsonProperty("ultimos")
    public void setUltimos(List<MonthDays> ultimos) {
        this.ultimos = ultimos;
    }

    /**
     * 
     * @return
     *     The months
     */
    @JsonProperty("months")
    public List<Months> getMonths() {
        return months;
    }

    /**
     * 
     * @param months
     *     The months
     */
    @JsonProperty("months")
    public void setMonths(List<Months> months) {
        this.months = months;
    }

    /**
     * 
     * @return
     *     The holidays
     */
    @JsonProperty("holidays")
    public List<Holidays> getHolidays() {
        return holidays;
    }

    /**
     * 
     * @param holidays
     *     The holidays
     */
    @JsonProperty("holidays")
    public void setHolidays(List<Holidays> holidays) {
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
        return new HashCodeBuilder().append(mode).append(dates).append(weekdays).append(monthdays).append(ultimos).append(months).append(holidays).append(intervals).toHashCode();
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
        return new EqualsBuilder().append(mode, rhs.mode).append(dates, rhs.dates).append(weekdays, rhs.weekdays).append(monthdays, rhs.monthdays).append(ultimos, rhs.ultimos).append(months, rhs.months).append(holidays, rhs.holidays).append(intervals, rhs.intervals).isEquals();
    }

}
