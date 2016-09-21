
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent cluster filter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentClusterFilterSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    private List<AgentCluster> agentClusters = new ArrayList<AgentCluster>();
    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     */
    private String regex;
    private Integer state;
    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     */
    private Boolean compact = false;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The agentClusters
     */
    public List<AgentCluster> getAgentClusters() {
        return agentClusters;
    }

    /**
     * 
     * @param agentClusters
     *     The agentClusters
     */
    public void setAgentClusters(List<AgentCluster> agentClusters) {
        this.agentClusters = agentClusters;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @return
     *     The regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * filter with regex
     * <p>
     * regular expression to filter JobScheduler objects by matching the path
     * 
     * @param regex
     *     The regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * 
     * @return
     *     The state
     */
    public Integer getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @return
     *     The compact
     */
    public Boolean getCompact() {
        return compact;
    }

    /**
     * compact parameter
     * <p>
     * controls if the object view is compact or detailed
     * 
     * @param compact
     *     The compact
     */
    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(agentClusters).append(regex).append(state).append(compact).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterFilterSchema) == false) {
            return false;
        }
        AgentClusterFilterSchema rhs = ((AgentClusterFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(agentClusters, rhs.agentClusters).append(regex, rhs.regex).append(state, rhs.state).append(compact, rhs.compact).isEquals();
    }

}
