
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
    "agent"
})
public class AgentUrl {

    /**
     * Url of an Agent
     * 
     */
    @JsonProperty("agent")
    private String agent;

    /**
     * Url of an Agent
     * 
     * @return
     *     The agent
     */
    @JsonProperty("agent")
    public String getAgent() {
        return agent;
    }

    /**
     * Url of an Agent
     * 
     * @param agent
     *     The agent
     */
    @JsonProperty("agent")
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
        if ((other instanceof AgentUrl) == false) {
            return false;
        }
        AgentUrl rhs = ((AgentUrl) other);
        return new EqualsBuilder().append(agent, rhs.agent).isEquals();
    }

}
