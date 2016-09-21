
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Agent_ {

    /**
     * Url of an Agent
     * 
     */
    private String agent;

    /**
     * Url of an Agent
     * 
     * @return
     *     The agent
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Url of an Agent
     * 
     * @param agent
     *     The agent
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(agent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Agent_) == false) {
            return false;
        }
        Agent_ rhs = ((Agent_) other);
        return new EqualsBuilder().append(agent, rhs.agent).isEquals();
    }

}
