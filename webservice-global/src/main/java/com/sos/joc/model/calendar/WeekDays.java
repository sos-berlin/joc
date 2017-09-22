
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
 * weekdays
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "from",
    "to",
    "days"
})
public class WeekDays {

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
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("days")
    private List<Integer> days = null;

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
     * (Required)
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
     * (Required)
     * 
     * @param days
     *     The days
     */
    @JsonProperty("days")
    public void setDays(List<Integer> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).append(to).append(days).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WeekDays) == false) {
            return false;
        }
        WeekDays rhs = ((WeekDays) other);
        return new EqualsBuilder().append(from, rhs.from).append(to, rhs.to).append(days, rhs.days).isEquals();
    }

}
