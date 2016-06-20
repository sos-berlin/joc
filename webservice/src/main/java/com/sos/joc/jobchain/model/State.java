
package com.sos.joc.jobchain.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "severity",
    "text"
})
public class State {

    /**
     *  0=ok, with a green representation, etc.
     * 
     */
    @JsonProperty("severity")
    private State.Severity severity;
    @JsonProperty("text")
    private State.Text text;
    
    /**
     *  0=ok, with a green representation, etc.
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public State.Severity getSeverity() {
        return severity;
    }

    /**
     *  0=ok, with a green representation, etc.
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
     * @return
     *     The text
     */
    @JsonProperty("text")
    public State.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    @JsonProperty("text")
    public void setText(State.Text text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

     

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(severity).append(text).toHashCode();
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
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Severity {

        _0("0"),
        _1("1"),
        _2("2");
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

        NOT_INITIALIZED("not_initialized"),
        ACTIVE("active"),
        RUNNING("running"),
        STOPPED("stopped");
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
