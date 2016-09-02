
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class State____ {

    /**
     * TODO mapping severity and _text
     * 
     */
    private Integer severity;
    private State____.Text text;

    /**
     * TODO mapping severity and _text
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     * TODO mapping severity and _text
     * 
     * @param severity
     *     The severity
     */
    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    /**
     * 
     * @return
     *     The text
     */
    public State____.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The _text
     */
    public void setText(State____.Text text) {
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
        if ((other instanceof State____) == false) {
            return false;
        }
        State____ rhs = ((State____) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        NONE("NONE"),
        LOADING("LOADING"),
        STARTING("STARTING"),
        RUNNING("RUNNING"),
        RUNNING_PROCESS("RUNNING_PROCESS"),
        RUNNING_REMOTE_PROCESS("RUNNING_REMOTE_PROCESS"),
        WAITING_FOR_PROCESS("WAITING_FOR_PROCESS"),
        WAITING_FOR_LOCKS("WAITING_FOR_LOCKS"),
        WAITING_FOR_ORDER("WAITING_FOR_ORDER"),
        ENDING("ENDING"),
        CLOSED("CLOSED");
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

        @Override
        public String toString() {
            return this.value;
        }

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
