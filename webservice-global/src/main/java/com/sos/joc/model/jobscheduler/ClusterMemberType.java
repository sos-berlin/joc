
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler cluster member type
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "_type",
    "precedence"
})
public class ClusterMemberType {

    /**
     * jobscheduler cluster type
     * <p>
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     */
    @JsonProperty("_type")
    private ClusterType _type;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("precedence")
    private Integer precedence;

    /**
     * jobscheduler cluster type
     * <p>
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @return
     *     The _type
     */
    @JsonProperty("_type")
    public ClusterType get_type() {
        return _type;
    }

    /**
     * jobscheduler cluster type
     * <p>
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @param _type
     *     The _type
     */
    @JsonProperty("_type")
    public void set_type(ClusterType _type) {
        this._type = _type;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The precedence
     */
    @JsonProperty("precedence")
    public Integer getPrecedence() {
        return precedence;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param precedence
     *     The precedence
     */
    @JsonProperty("precedence")
    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_type).append(precedence).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMemberType) == false) {
            return false;
        }
        ClusterMemberType rhs = ((ClusterMemberType) other);
        return new EqualsBuilder().append(_type, rhs._type).append(precedence, rhs.precedence).isEquals();
    }

}
