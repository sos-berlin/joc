
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    "standalone",
    "active",
    "passive"
})
public class ClusterMemberType1Schema {

    /**
     * JobScheduler doesn't run in a cluster
     * 
     */
    @JsonProperty("standalone")
    private Boolean standalone;
    /**
     * JobScheduler is member of an active cluster (distributed orders)
     * 
     */
    @JsonProperty("active")
    private Boolean active;
    /**
     * JobScheduler is member of a passive cluster (backup)
     * 
     */
    @JsonProperty("passive")
    private Passive passive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * JobScheduler doesn't run in a cluster
     * 
     * @return
     *     The standalone
     */
    @JsonProperty("standalone")
    public Boolean getStandalone() {
        return standalone;
    }

    /**
     * JobScheduler doesn't run in a cluster
     * 
     * @param standalone
     *     The standalone
     */
    @JsonProperty("standalone")
    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    /**
     * JobScheduler is member of an active cluster (distributed orders)
     * 
     * @return
     *     The active
     */
    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    /**
     * JobScheduler is member of an active cluster (distributed orders)
     * 
     * @param active
     *     The active
     */
    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * JobScheduler is member of a passive cluster (backup)
     * 
     * @return
     *     The passive
     */
    @JsonProperty("passive")
    public Passive getPassive() {
        return passive;
    }

    /**
     * JobScheduler is member of a passive cluster (backup)
     * 
     * @param passive
     *     The passive
     */
    @JsonProperty("passive")
    public void setPassive(Passive passive) {
        this.passive = passive;
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
        return new HashCodeBuilder().append(standalone).append(active).append(passive).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(standalone, rhs.standalone).append(active, rhs.active).append(passive, rhs.passive).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
