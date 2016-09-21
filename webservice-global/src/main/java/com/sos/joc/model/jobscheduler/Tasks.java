
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Tasks {

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
    private Integer running = 0;

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
     *     The running
     */
    public Integer getRunning() {
        return running;
    }

    /**
     * 
     * (Required)
     * 
     * @param running
     *     The running
     */
    public void setRunning(Integer running) {
        this.running = running;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(any).append(running).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tasks) == false) {
            return false;
        }
        Tasks rhs = ((Tasks) other);
        return new EqualsBuilder().append(any, rhs.any).append(running, rhs.running).isEquals();
    }

}
