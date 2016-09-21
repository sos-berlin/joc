
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler cluster member type
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ClusterMemberTypeSchema {

    /**
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     */
    private String type;
    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     */
    private Integer precedence;

    /**
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     * @return
     *     The precedence
     */
    public Integer getPrecedence() {
        return precedence;
    }

    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
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
        return new HashCodeBuilder().append(type).append(precedence).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMemberTypeSchema) == false) {
            return false;
        }
        ClusterMemberTypeSchema rhs = ((ClusterMemberTypeSchema) other);
        return new EqualsBuilder().append(type, rhs.type).append(precedence, rhs.precedence).isEquals();
    }

}
