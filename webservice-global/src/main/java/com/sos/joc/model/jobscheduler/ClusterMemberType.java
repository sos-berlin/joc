
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
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
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     */
    @JsonProperty("_type")
    private ClusterMemberType._type _type;
    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     */
    @JsonProperty("precedence")
    private Integer precedence;

    /**
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @return
     *     The _type
     */
    @JsonProperty("_type")
    public ClusterMemberType._type get_type() {
        return _type;
    }

    /**
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @param _type
     *     The _type
     */
    @JsonProperty("_type")
    public void set_type(ClusterMemberType._type _type) {
        this._type = _type;
    }

    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     * @return
     *     The precedence
     */
    @JsonProperty("precedence")
    public Integer getPrecedence() {
        return precedence;
    }

    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
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

    @Generated("org.jsonschema2pojo")
    public enum _type {

        STANDALONE("standalone"),
        ACTIVE("active"),
        PASSIVE("passive");
        private final String value;
        private final static Map<String, ClusterMemberType._type> CONSTANTS = new HashMap<String, ClusterMemberType._type>();

        static {
            for (ClusterMemberType._type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private _type(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static ClusterMemberType._type fromValue(String value) {
            ClusterMemberType._type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
