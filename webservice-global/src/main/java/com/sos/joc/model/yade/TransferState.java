
package com.sos.joc.model.yade;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * transfer state
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
public class TransferState {

    /**
     *  0=SUCCESSFUL, 1=INCOMPLETE, 2=FAILED
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * transfer state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private TransferStateText _text;

    /**
     *  0=SUCCESSFUL, 1=INCOMPLETE, 2=FAILED
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
     *  0=SUCCESSFUL, 1=INCOMPLETE, 2=FAILED
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
     * transfer state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public TransferStateText get_text() {
        return _text;
    }

    /**
     * transfer state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(TransferStateText _text) {
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
        if ((other instanceof TransferState) == false) {
            return false;
        }
        TransferState rhs = ((TransferState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
