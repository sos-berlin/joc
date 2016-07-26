
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * JobScheduler cluster members
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "type",
    "members"
})
public class ClusterMembersSchema {

    /**
     * JobScheduler id of all cluster member
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * type could be 'active' (distributed orders), 'passive' (backup) or 'standalone' (without cluster)
     * (Required)
     * 
     */
    @JsonProperty("type")
    private ClusterMembersSchema.Type type;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("members")
    private List<ClusterMemberSchema> members = new ArrayList<ClusterMemberSchema>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * JobScheduler id of all cluster member
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * JobScheduler id of all cluster member
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * type could be 'active' (distributed orders), 'passive' (backup) or 'standalone' (without cluster)
     * (Required)
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public ClusterMembersSchema.Type getType() {
        return type;
    }

    /**
     * type could be 'active' (distributed orders), 'passive' (backup) or 'standalone' (without cluster)
     * (Required)
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(ClusterMembersSchema.Type type) {
        this.type = type;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The members
     */
    @JsonProperty("members")
    public List<ClusterMemberSchema> getMembers() {
        return members;
    }

    /**
     * 
     * (Required)
     * 
     * @param members
     *     The members
     */
    @JsonProperty("members")
    public void setMembers(List<ClusterMemberSchema> members) {
        this.members = members;
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
        return new HashCodeBuilder().append(jobschedulerId).append(type).append(members).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMembersSchema) == false) {
            return false;
        }
        ClusterMembersSchema rhs = ((ClusterMembersSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(type, rhs.type).append(members, rhs.members).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        ACTIVE("active"),
        PASSIVE("passive"),
        STANDALONE("standalone");
        private final String value;
        private final static Map<String, ClusterMembersSchema.Type> CONSTANTS = new HashMap<String, ClusterMembersSchema.Type>();

        static {
            for (ClusterMembersSchema.Type c: values()) {
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
        public static ClusterMembersSchema.Type fromValue(String value) {
            ClusterMembersSchema.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
