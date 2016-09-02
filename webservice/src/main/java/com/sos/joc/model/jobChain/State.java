
package com.sos.joc.model.jobChain;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class State {

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
     * 
     */
    private Integer severity;
    private State.Text text;

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=running, 4=active, 3=initialized, 2=under_construction/stopped/not_initialized
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
    public State.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The _text
     */
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
    public enum Text {

        NOT_INITIALIZED("NOT_INITIALIZED"),
        INITIALIZED("INITIALIZED"),
        ACTIVE("ACTIVE"),
        UNDER_CONSTRUCTION("UNDER_CONSTRUCTION"),
        STOPPED("STOPPED");
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

        @Override
        public String toString() {
            return this.value;
        }

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
