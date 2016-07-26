
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "agent"
})
public class Agent_ {

    /**
     * Url of an Agent
     * 
     */
    @JsonProperty("agent")
    private Pattern agent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Url of an Agent
     * 
     * @return
     *     The agent
     */
    @JsonProperty("agent")
    public Pattern getAgent() {
        return agent;
    }

    /**
     * Url of an Agent
     * 
     * @param agent
     *     The agent
     */
    @JsonProperty("agent")
    public void setAgent(Pattern agent) {
        this.agent = agent;
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
        return new HashCodeBuilder().append(agent).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(agent, rhs.agent).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
