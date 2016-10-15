
package com.sos.joc.model.plan;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * plan state
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
public class PlanState {

    /**
     *  0=SUCCESSFUL, 1=INCOMPLETE, 2=FAILED, 4=PLANNED
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * plan state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private PlanStateText _text;

    /**
     *  0=SUCCESSFUL, 1=INCOMPLETE, 2=FAILED, 4=PLANNED
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
     *  0=SUCCESSFUL, 1=INCOMPLETE, 2=FAILED, 4=PLANNED
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
     * plan state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public PlanStateText get_text() {
        return _text;
    }

    /**
     * plan state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(PlanStateText _text) {
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
        if ((other instanceof PlanState) == false) {
            return false;
        }
        PlanState rhs = ((PlanState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
