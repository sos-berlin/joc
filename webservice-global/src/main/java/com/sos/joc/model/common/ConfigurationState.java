
package com.sos.joc.model.common;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * configuration status
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "severity",
    "_text",
    "message"
})
public class ConfigurationState {

    /**
     *  4=ok; 5=replacement_is_standing_by,removing_delayed; 2=error_in_configuration_file,changed_file_not_loaded,resource_is_missing
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    /**
     * configuration status
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private ConfigurationStateText _text;
    /**
     * contains e.g. error message
     * 
     */
    @JsonProperty("message")
    private String message;

    /**
     *  4=ok; 5=replacement_is_standing_by,removing_delayed; 2=error_in_configuration_file,changed_file_not_loaded,resource_is_missing
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
     *  4=ok; 5=replacement_is_standing_by,removing_delayed; 2=error_in_configuration_file,changed_file_not_loaded,resource_is_missing
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
     * configuration status
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The _text
     */
    @JsonProperty("_text")
    public ConfigurationStateText get_text() {
        return _text;
    }

    /**
     * configuration status
     * <p>
     * 
     * (Required)
     * 
     * @param _text
     *     The _text
     */
    @JsonProperty("_text")
    public void set_text(ConfigurationStateText _text) {
        this._text = _text;
    }

    /**
     * contains e.g. error message
     * 
     * @return
     *     The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * contains e.g. error message
     * 
     * @param message
     *     The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(_text).append(message).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigurationState) == false) {
            return false;
        }
        ConfigurationState rhs = ((ConfigurationState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(_text, rhs._text).append(message, rhs.message).isEquals();
    }

}
