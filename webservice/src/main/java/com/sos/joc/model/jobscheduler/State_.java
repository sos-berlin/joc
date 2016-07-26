
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
 * agent cluster state
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
public class State_ {

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private State_.Severity severity;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("_text")
    private State_.Text text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public State_.Severity getSeverity() {
        return severity;
    }

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     * @param severity
     *     The severity
     */
    @JsonProperty("severity")
    public void setSeverity(State_.Severity severity) {
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
    public State_.Text getText() {
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
    public void setText(State_.Text text) {
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
        if ((other instanceof State_) == false) {
            return false;
        }
        State_ rhs = ((State_) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Severity {

        _0("0"),
        _1("1"),
        _2("2");
        private final String value;
        private final static Map<String, State_.Severity> CONSTANTS = new HashMap<String, State_.Severity>();

        static {
            for (State_.Severity c: values()) {
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
        public static State_.Severity fromValue(String value) {
            State_.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        ALL_AGENTS_ARE_RUNNING("all_agents_are_running"),
        ONLY_SOME_AGENTS_ARE_RUNNING("only_some_agents_are_running"),
        ALL_AGENTS_ARE_UNREACHABLE("all_agents_are_unreachable");
        private final String value;
        private final static Map<String, State_.Text> CONSTANTS = new HashMap<String, State_.Text>();

        static {
            for (State_.Text c: values()) {
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
        public static State_.Text fromValue(String value) {
            State_.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
