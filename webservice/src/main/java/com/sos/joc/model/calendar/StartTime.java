
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * estimated time
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class StartTime {

    /**
     * hh:mm:ss
     * (Required)
     * 
     */
    private String time;
    /**
     * 
     * (Required)
     * 
     */
    private Boolean estimated;

    /**
     * hh:mm:ss
     * (Required)
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * hh:mm:ss
     * (Required)
     * 
     * @param time
     *     The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The estimated
     */
    public Boolean getEstimated() {
        return estimated;
    }

    /**
     * 
     * (Required)
     * 
     * @param estimated
     *     The estimated
     */
    public void setEstimated(Boolean estimated) {
        this.estimated = estimated;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(time).append(estimated).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartTime) == false) {
            return false;
        }
        StartTime rhs = ((StartTime) other);
        return new EqualsBuilder().append(time, rhs.time).append(estimated, rhs.estimated).isEquals();
    }

}
