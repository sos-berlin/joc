
package com.sos.joc.model.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * holiday
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "weekdays",
    "dates"
})
public class HolidaySchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("weekdays")
    private List<Integer> weekdays = new ArrayList<Integer>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dates")
    private List<String> dates = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The weekdays
     */
    @JsonProperty("weekdays")
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
    @JsonProperty("weekdays")
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
    @JsonProperty("dates")
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
    @JsonProperty("dates")
    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(weekdays).append(dates).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(weekdays, rhs.weekdays).append(dates, rhs.dates).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
