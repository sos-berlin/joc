
package com.sos.joc.model.order;

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

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "severity",
    "_text"
})
public class ProcessingState {

    @JsonProperty("severity")
    private ProcessingState.Severity severity;
    @JsonProperty("_text")
    private ProcessingState.Text text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public ProcessingState.Severity getSeverity() {
        return severity;
    }

    /**
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(ProcessingState.Severity severity) {
        this.severity = severity;
    }

    /**
     * 
     * @return
     *     The text
     */
    @JsonProperty("_text")
    public ProcessingState.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The _text
     */
    @JsonProperty("_text")
    public void setText(ProcessingState.Text text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(text).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProcessingState) == false) {
            return false;
        }
        ProcessingState rhs = ((ProcessingState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Severity {

        _3("3");
        private final String value;
        private final static Map<String, ProcessingState.Severity> CONSTANTS = new HashMap<String, ProcessingState.Severity>();

        static {
            for (ProcessingState.Severity c: values()) {
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
        public static ProcessingState.Severity fromValue(String value) {
            ProcessingState.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        BACKLIST("backlist");
        private final String value;
        private final static Map<String, ProcessingState.Text> CONSTANTS = new HashMap<String, ProcessingState.Text>();

        static {
            for (ProcessingState.Text c: values()) {
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
        public static ProcessingState.Text fromValue(String value) {
            ProcessingState.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
