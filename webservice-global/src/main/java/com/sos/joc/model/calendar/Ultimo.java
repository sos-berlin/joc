
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
 * ultimo
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
    "lastWorkingDays",
    "firstWorkingDays"
})
public class Ultimo {

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
    private List<String> days = new ArrayList<String>();
    @JsonProperty("lastWorkingDays")
    private List<String> lastWorkingDays = new ArrayList<String>();
    @JsonProperty("firstWorkingDays")
    private List<String> firstWorkingDays = new ArrayList<String>();

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
     *     The lastWorkingDays
     */
    @JsonProperty("lastWorkingDays")
    public List<String> getLastWorkingDays() {
        return lastWorkingDays;
    }

    /**
     * 
     * @param lastWorkingDays
     *     The lastWorkingDays
     */
    @JsonProperty("lastWorkingDays")
    public void setLastWorkingDays(List<String> lastWorkingDays) {
        this.lastWorkingDays = lastWorkingDays;
    }

    /**
     * 
     * @return
     *     The firstWorkingDays
     */
    @JsonProperty("firstWorkingDays")
    public List<String> getFirstWorkingDays() {
        return firstWorkingDays;
    }

    /**
     * 
     * @param firstWorkingDays
     *     The firstWorkingDays
     */
    @JsonProperty("firstWorkingDays")
    public void setFirstWorkingDays(List<String> firstWorkingDays) {
        this.firstWorkingDays = firstWorkingDays;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(from).append(to).append(days).append(lastWorkingDays).append(firstWorkingDays).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Ultimo) == false) {
            return false;
        }
        Ultimo rhs = ((Ultimo) other);
        return new EqualsBuilder().append(from, rhs.from).append(to, rhs.to).append(days, rhs.days).append(lastWorkingDays, rhs.lastWorkingDays).append(firstWorkingDays, rhs.firstWorkingDays).isEquals();
    }

}
