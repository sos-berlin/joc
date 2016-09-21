
package com.sos.joc.model.jobChain;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class State_ {

    /**
     *  5=skipped, 4=active, 2=stopped
     * 
     */
    private Integer severity;
    private State_.Text text;

    /**
     *  5=skipped, 4=active, 2=stopped
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  5=skipped, 4=active, 2=stopped
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
    public State_.Text getText() {
        return text;
    }

    /**
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

        ACTIVE("ACTIVE"),
        SKIPPED("SKIPPED"),
        STOPPED("STOPPED");
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
