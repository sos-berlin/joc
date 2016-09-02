
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class JobChains {

    /**
     * 
     * (Required)
     * 
     */
    private Integer any = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer stopped = 0;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The any
     */
    public Integer getAny() {
        return any;
    }

    /**
     * 
     * (Required)
     * 
     * @param any
     *     The any
     */
    public void setAny(Integer any) {
        this.any = any;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The stopped
     */
    public Integer getStopped() {
        return stopped;
    }

    /**
     * 
     * (Required)
     * 
     * @param stopped
     *     The stopped
     */
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(any).append(stopped).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChains) == false) {
            return false;
        }
        JobChains rhs = ((JobChains) other);
        return new EqualsBuilder().append(any, rhs.any).append(stopped, rhs.stopped).isEquals();
    }

}
