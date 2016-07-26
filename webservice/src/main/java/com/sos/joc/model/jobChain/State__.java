
package com.sos.joc.model.jobChain;

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
public class State__ {

    /**
     *  0=running, 1=active
     * 
     */
    @JsonProperty("severity")
    private State__.Severity severity;
    @JsonProperty("_text")
    private State__.Text text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *  0=running, 1=active
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public State__.Severity getSeverity() {
        return severity;
    }

    /**
     *  0=running, 1=active
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(State__.Severity severity) {
        this.severity = severity;
    }

    /**
     * 
     * @return
     *     The text
     */
    @JsonProperty("_text")
    public State__.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The _text
     */
    @JsonProperty("_text")
    public void setText(State__.Text text) {
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
        if ((other instanceof State__) == false) {
            return false;
        }
        State__ rhs = ((State__) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Severity {

        _0("0"),
        _1("1"),
        _2("2");
        private final String value;
        private final static Map<String, State__.Severity> CONSTANTS = new HashMap<String, State__.Severity>();

        static {
            for (State__.Severity c: values()) {
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
        public static State__.Severity fromValue(String value) {
            State__.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        NOT_INITIALIZED("not_initialized"),
        ACTIVE("active"),
        RUNNING("running"),
        STOPPED("stopped");
        private final String value;
        private final static Map<String, State__.Text> CONSTANTS = new HashMap<String, State__.Text>();

        static {
            for (State__.Text c: values()) {
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
        public static State__.Text fromValue(String value) {
            State__.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
