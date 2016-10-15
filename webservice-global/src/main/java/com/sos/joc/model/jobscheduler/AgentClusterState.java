
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent cluster state
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "severity",
    "_text"
})
public class AgentClusterState {

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * agent cluster state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private AgentClusterStateText _text;

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     * agent cluster state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public AgentClusterStateText get_text() {
        return _text;
    }

    /**
     * agent cluster state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(AgentClusterStateText _text) {
        this._text = _text;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(_text).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AgentClusterState) == false) {
            return false;
        }
        AgentClusterState rhs = ((AgentClusterState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
