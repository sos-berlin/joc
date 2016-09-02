
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler state
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class State {

    /**
     *  0=running, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     */
    private Integer severity;
    /**
     * 
     * (Required)
     * 
     */
    private State.Text text;

    /**
     *  0=running, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=running, 1=paused, 3=waiting_for_activation/terminating, 2=waiting_for_database/dead/unreachable
     * (Required)
     * 
     * @param severity
     *     The severity
     */
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

        RUNNING("RUNNING"),
        PAUSED("PAUSED"),
        WAITING_FOR_ACTIVATION("WAITING_FOR_ACTIVATION"),
        TERMINATING("TERMINATING"),
        WAITING_FOR_DATABASE("WAITING_FOR_DATABASE"),
        DEAD("DEAD"),
        UNREACHABLE("UNREACHABLE");
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
