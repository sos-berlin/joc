
package com.sos.joc.model.event;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * comes with event OrderStepEnded
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "required",
    "type",
    "returnCode"
})
public class NodeTransition {

    @JsonProperty("required")
    private Object required;
    @JsonProperty("type")
    private NodeTransitionType type;
    /**
     * if type=ERROR
     * 
     */
    @JsonProperty("returnCode")
    private Integer returnCode;

    /**
     * 
     * @return
     *     The required
     */
    @JsonProperty("required")
    public Object getRequired() {
        return required;
    }

    /**
     * 
     * @param required
     *     The required
     */
    @JsonProperty("required")
    public void setRequired(Object required) {
        this.required = required;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public NodeTransitionType getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(NodeTransitionType type) {
        this.type = type;
    }

    /**
     * if type=ERROR
     * 
     * @return
     *     The returnCode
     */
    @JsonProperty("returnCode")
    public Integer getReturnCode() {
        return returnCode;
    }

    /**
     * if type=ERROR
     * 
     * @param returnCode
     *     The returnCode
     */
    @JsonProperty("returnCode")
    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(required).append(type).append(returnCode).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NodeTransition) == false) {
            return false;
        }
        NodeTransition rhs = ((NodeTransition) other);
        return new EqualsBuilder().append(required, rhs.required).append(type, rhs.type).append(returnCode, rhs.returnCode).isEquals();
    }

}
