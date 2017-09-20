
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
 * holidays
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "nationalCalendar",
    "dates"
})
public class Holidays {

    @JsonProperty("nationalCalendar")
    private String nationalCalendar;
    @JsonProperty("dates")
    private List<String> dates = null;

    /**
     * 
     * @return
     *     The nationalCalendar
     */
    @JsonProperty("nationalCalendar")
    public String getNationalCalendar() {
        return nationalCalendar;
    }

    /**
     * 
     * @param nationalCalendar
     *     The nationalCalendar
     */
    @JsonProperty("nationalCalendar")
    public void setNationalCalendar(String nationalCalendar) {
        this.nationalCalendar = nationalCalendar;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nationalCalendar).append(dates).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Holidays) == false) {
            return false;
        }
        Holidays rhs = ((Holidays) other);
        return new EqualsBuilder().append(nationalCalendar, rhs.nationalCalendar).append(dates, rhs.dates).isEquals();
    }

}
