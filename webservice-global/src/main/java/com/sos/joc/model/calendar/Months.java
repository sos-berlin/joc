
package com.sos.joc.model.calendar;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * month
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "months",
    "from",
    "to",
    "weekdays",
    "monthdays",
    "ultimos"
})
public class Months {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("months")
    private List<Integer> months = null;
    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("from")
    private String from;
    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("to")
    private String to;
    @JsonProperty("weekdays")
    private List<WeekDays> weekdays = null;
    @JsonProperty("monthdays")
    private List<MonthDays> monthdays = null;
    @JsonProperty("ultimos")
    private List<MonthDays> ultimos = null;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The months
     */
    @JsonProperty("months")
    public List<Integer> getMonths() {
        return months;
    }

    /**
     * 
     * (Required)
     * 
     * @param months
     *     The months
     */
    @JsonProperty("months")
    public void setMonths(List<Integer> months) {
        this.months = months;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @return
     *     The from
     */
    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @param from
     *     The from
     */
    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @return
     *     The to
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @param to
     *     The to
     */
    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(months).append(from).append(to).append(weekdays).append(monthdays).append(ultimos).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Months) == false) {
            return false;
        }
        Months rhs = ((Months) other);
        return new EqualsBuilder().append(months, rhs.months).append(from, rhs.from).append(to, rhs.to).append(weekdays, rhs.weekdays).append(monthdays, rhs.monthdays).append(ultimos, rhs.ultimos).isEquals();
    }

}
