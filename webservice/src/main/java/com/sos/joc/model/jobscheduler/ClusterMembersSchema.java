
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobScheduler cluster members
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ClusterMembersSchema {

    /**
     * JobScheduler id of all cluster member
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * type could be 'active' (distributed orders), 'passive' (backup) or 'standalone' (without cluster)
     * (Required)
     * 
     */
    private String type;
    /**
     * 
     * (Required)
     * 
     */
    private List<ClusterMemberSchema> members = new ArrayList<ClusterMemberSchema>();

    /**
     * JobScheduler id of all cluster member
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
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
    public String getType() {
        return type;
    }

    /**
     * type could be 'active' (distributed orders), 'passive' (backup) or 'standalone' (without cluster)
     * (Required)
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The members
     */
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
    public void setMembers(List<ClusterMemberSchema> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(type).append(members).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(type, rhs.type).append(members, rhs.members).isEquals();
    }

}
