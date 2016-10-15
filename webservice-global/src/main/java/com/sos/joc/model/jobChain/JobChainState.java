
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChain state
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
public class JobChainState {

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * jobChain state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private JobChainStateText _text;

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
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
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
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
     * jobChain state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public JobChainStateText get_text() {
        return _text;
    }

    /**
     * jobChain state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(JobChainStateText _text) {
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
        if ((other instanceof JobChainState) == false) {
            return false;
        }
        JobChainState rhs = ((JobChainState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
