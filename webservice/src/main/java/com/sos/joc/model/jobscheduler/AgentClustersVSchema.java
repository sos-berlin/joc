
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent clusters (volatile part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentClustersVSchema {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    private List<AgentClusterVSchema> agentClusters = new ArrayList<AgentClusterVSchema>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The agentClusters
     */
    public List<AgentClusterVSchema> getAgentClusters() {
        return agentClusters;
    }

    /**
     * 
     * (Required)
     * 
     * @param agentClusters
     *     The agentClusters
     */
    public void setAgentClusters(List<AgentClusterVSchema> agentClusters) {
        this.agentClusters = agentClusters;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(agentClusters).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClustersVSchema) == false) {
            return false;
        }
        AgentClustersVSchema rhs = ((AgentClustersVSchema) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(agentClusters, rhs.agentClusters).isEquals();
    }

}
