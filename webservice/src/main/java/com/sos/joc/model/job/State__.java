
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
public class State__ {

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
    private State__.Text text;
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
    public State__.Text getText() {
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
    public enum Text {

        initialized("initialized"),
        not_initialized("not_initialized"),
        loaded("loaded"),
        pending("pending"),
        running("running"),
        waiting_for_process("waiting_for_process"),
        waiting_for_lock("waiting_for_lock"),
        waiting_for_agent("waiting_for_agent"),
        waiting_for_task("waiting_for_task"),
        not_in_period("not_in_period"),
        stopping("stopping"),
        stopped("stopped"),
        removed("removed"),
        disabled("disabled");
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
