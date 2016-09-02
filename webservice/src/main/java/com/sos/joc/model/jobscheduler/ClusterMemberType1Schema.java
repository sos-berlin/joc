
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
public class ClusterMemberType1Schema {

    /**
     * JobScheduler doesn't run in a cluster
     * 
     */
    private Boolean standalone;
    /**
     * JobScheduler is member of an active cluster (distributed orders)
     * 
     */
    private Boolean active;
    /**
     * JobScheduler is member of a passive cluster (backup)
     * 
     */
    private Passive passive;

    /**
     * JobScheduler doesn't run in a cluster
     * 
     * @return
     *     The standalone
     */
    public Boolean getStandalone() {
        return standalone;
    }

    /**
     * JobScheduler doesn't run in a cluster
     * 
     * @param standalone
     *     The standalone
     */
    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    /**
     * JobScheduler is member of an active cluster (distributed orders)
     * 
     * @return
     *     The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * JobScheduler is member of an active cluster (distributed orders)
     * 
     * @param active
     *     The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * JobScheduler is member of a passive cluster (backup)
     * 
     * @return
     *     The passive
     */
    public Passive getPassive() {
        return passive;
    }

    /**
     * JobScheduler is member of a passive cluster (backup)
     * 
     * @param passive
     *     The passive
     */
    public void setPassive(Passive passive) {
        this.passive = passive;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(standalone).append(active).append(passive).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClusterMemberType1Schema) == false) {
            return false;
        }
        ClusterMemberType1Schema rhs = ((ClusterMemberType1Schema) other);
        return new EqualsBuilder().append(standalone, rhs.standalone).append(active, rhs.active).append(passive, rhs.passive).isEquals();
    }

}
