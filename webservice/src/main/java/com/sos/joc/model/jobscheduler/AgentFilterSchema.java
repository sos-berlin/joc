
package com.sos.joc.model.jobscheduler;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agents filter
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class AgentFilterSchema {

    private String jobschedulerId;
    private List<Agent_> agents = new ArrayList<Agent_>();

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
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
     *     The agents
     */
    public List<Agent_> getAgents() {
        return agents;
    }

    /**
     * 
     * @param agents
     *     The agents
     */
    public void setAgents(List<Agent_> agents) {
        this.agents = agents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(agents).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentFilterSchema) == false) {
            return false;
        }
        AgentFilterSchema rhs = ((AgentFilterSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(agents, rhs.agents).isEquals();
    }

}
