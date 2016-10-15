
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * JobScheduler cluster members
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "_type",
    "members"
})
public class ClusterMembers {

    /**
     * JobScheduler id of all cluster member
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     */
    @JsonProperty("_type")
    private ClusterMembers._type _type;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("members")
    private List<ClusterMember> members = new ArrayList<ClusterMember>();

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
     * Possible values are: 'standalone','active','passive'; JobScheduler doesn't run in a cluster (standalone) or is member of an active (distributed orders) or passive cluster (backup)
     * (Required)
     * 
     * @return
     *     The _type
     */
    @JsonProperty("_type")
    public ClusterMembers._type get_type() {
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
    public void set_type(ClusterMembers._type _type) {
        this._type = _type;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The members
     */
    @JsonProperty("members")
    public List<ClusterMember> getMembers() {
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
    public void setMembers(List<ClusterMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(_type).append(members).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMembers) == false) {
            return false;
        }
        ClusterMembers rhs = ((ClusterMembers) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(_type, rhs._type).append(members, rhs.members).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum _type {

        STANDALONE("standalone"),
        ACTIVE("active"),
        PASSIVE("passive");
        private final String value;
        private final static Map<String, ClusterMembers._type> CONSTANTS = new HashMap<String, ClusterMembers._type>();

        static {
            for (ClusterMembers._type c: values()) {
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
        public static ClusterMembers._type fromValue(String value) {
            ClusterMembers._type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
