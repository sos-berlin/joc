
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class State__ {

    /**
     *  0=running; 2=unreachable
     * (Required)
     * 
     */
    private Integer severity;
    /**
     * 
     * (Required)
     * 
     */
    private State__.Text text;

    /**
     *  0=running; 2=unreachable
     * (Required)
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=running; 2=unreachable
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
    public void setText(State__.Text text) {
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
        if ((other instanceof State__) == false) {
            return false;
        }
        State__ rhs = ((State__) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        RUNNING("RUNNING"),
        UNREACHABLE("UNREACHABLE");
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

        @Override
        public String toString() {
            return this.value;
        }

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
