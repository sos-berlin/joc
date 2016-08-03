
package com.sos.joc.model.jobscheduler;

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
 * jobscheduler state
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
public class State {

    /**
     *  0=running, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private State.Severity severity;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private State.Text text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *  0=running, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public State.Severity getSeverity() {
        return severity;
    }

    /**
     *  0=running, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(State.Severity severity) {
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
    public State.Text getText() {
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
    public void setText(State.Text text) {
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
        if ((other instanceof State) == false) {
            return false;
        }
        State rhs = ((State) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Severity {

        _0("0"),
        _1("1"),
        _2("2"),
        _3("3");
        private final String value;
        private final static Map<String, State.Severity> CONSTANTS = new HashMap<String, State.Severity>();

        static {
            for (State.Severity c: values()) {
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
        public static State.Severity fromValue(String value) {
            State.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        RUNNING("running"),
        PAUSED("paused"),
        WAITING_FOR_ACTIVATION("waiting_for_activation"),
        TERMINATING("terminating"),
        WAITING_FOR_DATABASE("waiting_for_database"),
        DEAD("dead"),
        UNREACHABLE("unreachable");
        private final String value;
        private final static Map<String, State.Text> CONSTANTS = new HashMap<String, State.Text>();

        static {
            for (State.Text c: values()) {
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
        public static State.Text fromValue(String value) {
            State.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
