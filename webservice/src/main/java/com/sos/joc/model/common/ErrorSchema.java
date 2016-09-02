
package com.sos.joc.model.common;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * error
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ErrorSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String code;
    /**
     * 
     * (Required)
     * 
     */
    private String message;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * (Required)
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * (Required)
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(message).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ErrorSchema) == false) {
            return false;
        }
        ErrorSchema rhs = ((ErrorSchema) other);
        return new EqualsBuilder().append(code, rhs.code).append(message, rhs.message).isEquals();
    }

}
