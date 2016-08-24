
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    "type",
    "precedence"
})
public class ClusterMemberTypeSchema {

    /**
     * JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     */
    @JsonProperty("type")
    private ClusterMemberTypeSchema.Type type;
    /**
     * primary (0), secondary (1), ...only relevant for passive cluster
     * 
     */
    @JsonProperty("precedence")
    private Integer precedence;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public ClusterMemberTypeSchema.Type getType() {
        return type;
    }

    /**
     * JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(ClusterMemberTypeSchema.Type type) {
        this.type = type;
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
        return new HashCodeBuilder().append(type).append(precedence).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(type, rhs.type).append(precedence, rhs.precedence).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        active("active"),
        passive("passive"),
        standalone("standalone");
        private final String value;
        private final static Map<String, ClusterMemberTypeSchema.Type> CONSTANTS = new HashMap<String, ClusterMemberTypeSchema.Type>();

        static {
            for (ClusterMemberTypeSchema.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static ClusterMemberTypeSchema.Type fromValue(String value) {
            ClusterMemberTypeSchema.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
