
package com.sos.joc.model.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
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
public class ConfigurationStatusSchema {

    /**
     *  0=ok, 1=warning, 2=error
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private ConfigurationStatusSchema.Severity severity;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private ConfigurationStatusSchema.Text text;
    /**
     * contains e.g. error message
     * 
     */
    @JsonProperty("message")
    private String message;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

    /**
     *  0=ok, 1=warning, 2=error
     * (Required)
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public ConfigurationStatusSchema.Severity getSeverity() {
        return severity;
    }

    /**
     *  0=ok, 1=warning, 2=error
     * (Required)
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(ConfigurationStatusSchema.Severity severity) {
        this.severity = severity;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The text
     */
    @JsonProperty("_text")
    public ConfigurationStatusSchema.Text getText() {
        return text;
    }

    /**
     * 
     * (Required)
     * 
     * @param text
     *     The _text
     */
    @JsonProperty("_text")
    public void setText(ConfigurationStatusSchema.Text text) {
        this.text = text;
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

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, java.lang.Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(text).append(message).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ConfigurationStatusSchema) == false) {
            return false;
        }
        ConfigurationStatusSchema rhs = ((ConfigurationStatusSchema) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(message, rhs.message).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Severity {

        _0("0"),
        _1("1"),
        _2("2");
        private final String value;
        private final static Map<String, ConfigurationStatusSchema.Severity> CONSTANTS = new HashMap<String, ConfigurationStatusSchema.Severity>();

        static {
            for (ConfigurationStatusSchema.Severity c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Severity(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static ConfigurationStatusSchema.Severity fromValue(String value) {
            ConfigurationStatusSchema.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        ERROR_IN_CONFIGURATION_FILE("error_in_configuration_file"),
        CHANGED_FILE_NOT_LOADED("changed_file_not_loaded"),
        REMOVING_DELAYED("removing_delayed"),
        RESOURCE_IS_MISSING("resource_is_missing"),
        REPLACEMENT_IS_STANDING_BY("replacement_is_standing_by"),
        OK("ok");
        private final String value;
        private final static Map<String, ConfigurationStatusSchema.Text> CONSTANTS = new HashMap<String, ConfigurationStatusSchema.Text>();

        static {
            for (ConfigurationStatusSchema.Text c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Text(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static ConfigurationStatusSchema.Text fromValue(String value) {
            ConfigurationStatusSchema.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
