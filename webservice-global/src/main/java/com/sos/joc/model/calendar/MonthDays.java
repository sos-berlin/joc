
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
 * DaysOfMonth for MonthDays or Ultimos
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "from",
    "to",
    "days",
    "workingDays",
    "weeklyDays"
})
public class MonthDays {

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
    @JsonProperty("days")
    private List<Integer> days = null;
    @JsonProperty("workingDays")
    private List<Integer> workingDays = null;
    @JsonProperty("weeklyDays")
    private List<WeeklyDay> weeklyDays = null;

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
     *     The days
     */
    @JsonProperty("days")
    public List<Integer> getDays() {
        return days;
    }

    /**
     * 
     * @param days
     *     The days
     */
    @JsonProperty("days")
    public void setDays(List<Integer> days) {
        this.days = days;
    }

    /**
     * 
     * @return
     *     The workingDays
     */
    @JsonProperty("workingDays")
    public List<Integer> getWorkingDays() {
        return workingDays;
    }

    /**
     * 
     * @param workingDays
     *     The workingDays
     */
    @JsonProperty("workingDays")
    public void setWorkingDays(List<Integer> workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * 
     * @return
     *     The weeklyDays
     */
    @JsonProperty("weeklyDays")
    public List<WeeklyDay> getWeeklyDays() {
        return weeklyDays;
    }

    /**
     * 
     * @param weeklyDays
     *     The weeklyDays
     */
    @JsonProperty("weeklyDays")
    public void setWeeklyDays(List<WeeklyDay> weeklyDays) {
        this.weeklyDays = weeklyDays;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).append(to).append(days).append(workingDays).append(weeklyDays).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MonthDays) == false) {
            return false;
        }
        MonthDays rhs = ((MonthDays) other);
        return new EqualsBuilder().append(from, rhs.from).append(to, rhs.to).append(days, rhs.days).append(workingDays, rhs.workingDays).append(weeklyDays, rhs.weeklyDays).isEquals();
    }

}
