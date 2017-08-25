
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
 * month
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "month",
    "from",
    "to",
    "weekdays",
    "monthdays",
    "ultimos"
})
public class Month {

    /**
     *  1-12 repeatable
     * (Required)
     * 
     */
    @JsonProperty("month")
    private String month;
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
    private List<String> weekdays = new ArrayList<String>();
    @JsonProperty("monthdays")
    private List<Monthday> monthdays = new ArrayList<Monthday>();
    @JsonProperty("ultimos")
    private List<Ultimo> ultimos = new ArrayList<Ultimo>();

    /**
     *  1-12 repeatable
     * (Required)
     * 
     * @return
     *     The month
     */
    @JsonProperty("month")
    public String getMonth() {
        return month;
    }

    /**
     *  1-12 repeatable
     * (Required)
     * 
     * @param month
     *     The month
     */
    @JsonProperty("month")
    public void setMonth(String month) {
        this.month = month;
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

    /**
     * 
     * @return
     *     The monthdays
     */
    @JsonProperty("monthdays")
    public List<Monthday> getMonthdays() {
        return monthdays;
    }

    /**
     * 
     * @param monthdays
     *     The monthdays
     */
    @JsonProperty("monthdays")
    public void setMonthdays(List<Monthday> monthdays) {
        this.monthdays = monthdays;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(month).append(from).append(to).append(weekdays).append(monthdays).append(ultimos).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Month) == false) {
            return false;
        }
        Month rhs = ((Month) other);
        return new EqualsBuilder().append(month, rhs.month).append(from, rhs.from).append(to, rhs.to).append(weekdays, rhs.weekdays).append(monthdays, rhs.monthdays).append(ultimos, rhs.ultimos).isEquals();
    }

}
