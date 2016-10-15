
package com.sos.joc.model.order;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * orderHistory state
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
public class OrderHistoryState {

    /**
     *  0=successful, 1=incomplete, 2=failed with a green/yellow/red representation
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * orderHistory state text
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private OrderHistoryStateText _text;

    /**
     *  0=successful, 1=incomplete, 2=failed with a green/yellow/red representation
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
     *  0=successful, 1=incomplete, 2=failed with a green/yellow/red representation
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
     * orderHistory state text
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public OrderHistoryStateText get_text() {
        return _text;
    }

    /**
     * orderHistory state text
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(OrderHistoryStateText _text) {
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
        if ((other instanceof OrderHistoryState) == false) {
            return false;
        }
        OrderHistoryState rhs = ((OrderHistoryState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).isEquals();
    }

}
