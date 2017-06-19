
package com.sos.joc.model.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job state
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
public class JobState {

    /**
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * job state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private JobStateText _text;

    /**
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown
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
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/error, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled/unknown
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
     * job state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public JobStateText get_text() {
        return _text;
    }

    /**
     * job state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(JobStateText _text) {
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
        if ((other instanceof JobState) == false) {
            return false;
        }
        JobState rhs = ((JobState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
