
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobScheduler is member of a passive cluster (backup)
 * 
 */
@Generated("org.jsonschema2pojo")
public class Passive {

    private Boolean active;
    /**
     * primary
     * 
     */
    private Integer precedence;

    /**
     * 
     * @return
     *     The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * primary
     * 
     * @return
     *     The precedence
     */
    public Integer getPrecedence() {
        return precedence;
    }

    /**
     * primary
     * 
     * @param precedence
     *     The precedence
     */
    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(active).append(precedence).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Passive) == false) {
            return false;
        }
        Passive rhs = ((Passive) other);
        return new EqualsBuilder().append(active, rhs.active).append(precedence, rhs.precedence).isEquals();
    }

}
