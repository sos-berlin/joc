
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler state
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
public class JobSchedulerState {

    /**
     *  0=running/starting, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * jobscheduler state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private JobSchedulerStateText _text;

    /**
     *  0=running/starting, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
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
     *  0=running/starting, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
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
     * jobscheduler state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public JobSchedulerStateText get_text() {
        return _text;
    }

    /**
     * jobscheduler state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(JobSchedulerStateText _text) {
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
        if ((other instanceof JobSchedulerState) == false) {
            return false;
        }
        JobSchedulerState rhs = ((JobSchedulerState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
