
package com.sos.joc.model.job;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class ProcessingState {

    /**
     *  1=pending; 0=running; 2=waiting_for_agent,job_chain_stopped,node_stopped,job_stopped; 5=setback,suspended; 3=job_not_in_period,node_delay,waiting_for_lock,waiting_for_process,waiting_for_task
     * (Required)
     * 
     */
    private Integer severity;
    /**
     * 
     * (Required)
     * 
     */
    private ProcessingState.Text text;

    /**
     *  1=pending; 0=running; 2=waiting_for_agent,job_chain_stopped,node_stopped,job_stopped; 5=setback,suspended; 3=job_not_in_period,node_delay,waiting_for_lock,waiting_for_process,waiting_for_task
     * (Required)
     * 
     * @return
     *     The severity
     */
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  1=pending; 0=running; 2=waiting_for_agent,job_chain_stopped,node_stopped,job_stopped; 5=setback,suspended; 3=job_not_in_period,node_delay,waiting_for_lock,waiting_for_process,waiting_for_task
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
    public ProcessingState.Text getText() {
        return text;
    }

    /**
     * 
     * (Required)
     * 
     * @param text
     *     The _text
     */
    public void setText(ProcessingState.Text text) {
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
        if ((other instanceof ProcessingState) == false) {
            return false;
        }
        ProcessingState rhs = ((ProcessingState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        PENDING("PENDING"),
        RUNNING("RUNNING"),
        SUSPENDED("SUSPENDED"),
        SETBACK("SETBACK"),
        BLACKLIST("BLACKLIST"),
        JOB_NOT_IN_PERIOD("JOB_NOT_IN_PERIOD"),
        NODE_DELAY("NODE_DELAY"),
        WAITING_FOR_LOCK("WAITING_FOR_LOCK"),
        WAITING_FOR_PROCESS("WAITING_FOR_PROCESS"),
        WAITING_FOR_AGENT("WAITING_FOR_AGENT"),
        JOB_CHAIN_STOPPED("JOB_CHAIN_STOPPED"),
        NODE_STOPPED("NODE_STOPPED"),
        JOB_STOPPED("JOB_STOPPED"),
        WAITING_FOR_TASK("WAITING_FOR_TASK");
        private final String value;
        private final static Map<String, ProcessingState.Text> CONSTANTS = new HashMap<String, ProcessingState.Text>();

        static {
            for (ProcessingState.Text c: values()) {
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

        public static ProcessingState.Text fromValue(String value) {
            ProcessingState.Text constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
