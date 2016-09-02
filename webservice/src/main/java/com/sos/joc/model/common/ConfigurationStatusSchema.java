
package com.sos.joc.model.common;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * configuration status
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class ConfigurationStatusSchema {

    /**
     *  4=ok; 5=replacement_is_standing_by,removing_delayed; 2=error_in_configuration_file,changed_file_not_loaded,resource_is_missing
     * (Required)
     * 
     */
    private Integer severity;
    /**
     * 
     * (Required)
     * 
     */
    private ConfigurationStatusSchema.Text text;
    /**
     * contains e.g. error message
     * 
     */
    private String message;

    /**
     *  4=ok; 5=replacement_is_standing_by,removing_delayed; 2=error_in_configuration_file,changed_file_not_loaded,resource_is_missing
     * (Required)
     * 
     * @return
     *     The severity
     */
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
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The text
     */
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
    public void setText(ConfigurationStatusSchema.Text text) {
        this.text = text;
    }

    /**
     * contains e.g. error message
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * contains e.g. error message
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
        return new HashCodeBuilder().append(severity).append(text).append(message).toHashCode();
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
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(message, rhs.message).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        ERROR_IN_CONFIGURATION_FILE("ERROR_IN_CONFIGURATION_FILE"),
        CHANGED_FILE_NOT_LOADED("CHANGED_FILE_NOT_LOADED"),
        REMOVING_DELAYED("REMOVING_DELAYED"),
        RESOURCE_IS_MISSING("RESOURCE_IS_MISSING"),
        REPLACEMENT_IS_STANDING_BY("REPLACEMENT_IS_STANDING_BY"),
        OK("OK");
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

        @Override
        public String toString() {
            return this.value;
        }

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
