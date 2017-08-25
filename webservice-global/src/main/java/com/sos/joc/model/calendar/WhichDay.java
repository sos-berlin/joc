
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * whichday
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "day",
    "which"
})
public class WhichDay {

    /**
     * day number
     * <p>
     *  0-7 repeatable
     * 
     */
    @JsonProperty("day")
    private String day;
    @JsonProperty("which")
    private Integer which;

    /**
     * day number
     * <p>
     *  0-7 repeatable
     * 
     * @return
     *     The day
     */
    @JsonProperty("day")
    public String getDay() {
        return day;
    }

    /**
     * day number
     * <p>
     *  0-7 repeatable
     * 
     * @param day
     *     The day
     */
    @JsonProperty("day")
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * 
     * @return
     *     The which
     */
    @JsonProperty("which")
    public Integer getWhich() {
        return which;
    }

    /**
     * 
     * @param which
     *     The which
     */
    @JsonProperty("which")
    public void setWhich(Integer which) {
        this.which = which;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(day).append(which).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WhichDay) == false) {
            return false;
        }
        WhichDay rhs = ((WhichDay) other);
        return new EqualsBuilder().append(day, rhs.day).append(which, rhs.which).isEquals();
    }

}
