
package com.sos.joc.model.job;

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
public class State_ {

    /**
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/removed, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled
     * (Required)
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
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
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/removed, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled
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
     *  0=running; 1=pending; 2=not_initialized/waiting_for_agent/stopping/stopped/removed, 3=initialized/loaded/waiting_for_process/waiting_for_lock/waiting_for_task/not_in_period, 4=disabled
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
    public enum Text {

        INITIALIZED("initialized"),
        NOT_INITIALIZED("not_initialized"),
        LOADED("loaded"),
        PENDING("pending"),
        RUNNING("running"),
        WAITING_FOR_PROCESS("waiting_for_process"),
        WAITING_FOR_LOCK("waiting_for_lock"),
        WAITING_FOR_AGENT("waiting_for_agent"),
        WAITING_FOR_TASK("waiting_for_task"),
        NOT_IN_PERIOD("not_in_period"),
        STOPPING("stopping"),
        STOPPED("stopped"),
        REMOVED("removed"),
        DISABLED("disabled");
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
