
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "agentCluster"
})
public class AgentClusterPath {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("agentCluster")
    private String agentCluster;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The agentCluster
     */
    @JsonProperty("agentCluster")
    public String getAgentCluster() {
        return agentCluster;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param agentCluster
     *     The agentCluster
     */
    @JsonProperty("agentCluster")
    public void setAgentCluster(String agentCluster) {
        this.agentCluster = agentCluster;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(agentCluster).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterPath) == false) {
            return false;
        }
        AgentClusterPath rhs = ((AgentClusterPath) other);
        return new EqualsBuilder().append(agentCluster, rhs.agentCluster).isEquals();
    }

}
