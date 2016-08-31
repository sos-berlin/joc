
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
public class State____ {

    /**
     * TODO mapping severity and _text
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    @JsonProperty("_text")
    private State____.Text text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * TODO mapping severity and _text
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     * TODO mapping severity and _text
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
     * @return
     *     The text
     */
    @JsonProperty("_text")
    public State____.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The _text
     */
    @JsonProperty("_text")
    public void setText(State____.Text text) {
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
        if ((other instanceof State____) == false) {
            return false;
        }
        State____ rhs = ((State____) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        none("none"),
        loading("loading"),
        starting("starting"),
        running("running"),
        running_process("running_process"),
        running_remote_process("running_remote_process"),
        waiting_for_process("waiting_for_process"),
        waiting_for_locks("waiting_for_locks"),
        waiting_for_order("waiting_for_order"),
        ending("ending"),
        closed("closed");
        private final String value;
        private final static Map<String, State____.Text> CONSTANTS = new HashMap<String, State____.Text>();

        static {
            for (State____.Text c: values()) {
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
        public static State____.Text fromValue(String value) {
            State____.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
