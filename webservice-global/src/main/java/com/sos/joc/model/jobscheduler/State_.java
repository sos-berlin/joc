
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * agent cluster state
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class State_ {

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     */
    private Integer severity;
    /**
     * 
     * (Required)
     * 
     */
    private State_.Text text;

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
     * (Required)
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  0=all Agents are running, 1=some Agents are unreachable but not all, 2=all Agents are unreachable
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
    public void setText(State_.Text text) {
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
        if ((other instanceof State_) == false) {
            return false;
        }
        State_ rhs = ((State_) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        ALL_AGENTS_ARE_RUNNING("ALL_AGENTS_ARE_RUNNING"),
        ONLY_SOME_AGENTS_ARE_RUNNING("ONLY_SOME_AGENTS_ARE_RUNNING"),
        ALL_AGENTS_ARE_UNREACHABLE("ALL_AGENTS_ARE_UNREACHABLE");
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

        @Override
        public String toString() {
            return this.value;
        }

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
