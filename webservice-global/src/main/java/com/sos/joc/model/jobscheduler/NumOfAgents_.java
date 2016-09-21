
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class NumOfAgents_ {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer any;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer running;

    /**
     * non negative integer
     * <p>
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
     * non negative integer
     * <p>
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
     * non negative integer
     * <p>
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
     * non negative integer
     * <p>
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
        if ((other instanceof NumOfAgents_) == false) {
            return false;
        }
        NumOfAgents_ rhs = ((NumOfAgents_) other);
        return new EqualsBuilder().append(any, rhs.any).append(running, rhs.running).isEquals();
    }

}
