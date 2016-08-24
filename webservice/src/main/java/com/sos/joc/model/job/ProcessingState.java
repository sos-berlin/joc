
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
public class ProcessingState {

    /**
     *  1=pending; 0=running; 2=waiting_for_agent,job_chain_stopped,node_stopped,job_stopped; 5=setback,suspended; 3=job_not_in_period,node_delay,waiting_for_lock,waiting_for_process,waiting_for_task
     * 
     */
    @JsonProperty("severity")
    private Integer severity;
    @JsonProperty("_text")
    private ProcessingState.Text text;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *  1=pending; 0=running; 2=waiting_for_agent,job_chain_stopped,node_stopped,job_stopped; 5=setback,suspended; 3=job_not_in_period,node_delay,waiting_for_lock,waiting_for_process,waiting_for_task
     * 
     * @return
     *     The severity
     */
    @JsonProperty("severity")
    public Integer getSeverity() {
        return severity;
    }

    /**
     *  1=pending; 0=running; 2=waiting_for_agent,job_chain_stopped,node_stopped,job_stopped; 5=setback,suspended; 3=job_not_in_period,node_delay,waiting_for_lock,waiting_for_process,waiting_for_task
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
    public ProcessingState.Text getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The _text
     */
    @JsonProperty("_text")
    public void setText(ProcessingState.Text text) {
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
        if ((other instanceof ProcessingState) == false) {
            return false;
        }
        ProcessingState rhs = ((ProcessingState) other);
        return new EqualsBuilder().append(severity, rhs.severity).append(text, rhs.text).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Text {

        pending("pending"),
        running("running"),
        suspended("suspended"),
        setback("setback"),
        backlist("backlist"),
        job_not_in_period("job_not_in_period"),
        node_delay("node_delay"),
        waiting_for_lock("waiting_for_lock"),
        waiting_for_process("waiting_for_process"),
        waiting_for_agent("waiting_for_agent"),
        job_chain_stopped("job_chain_stopped"),
        node_stopped("node_stopped"),
        job_stopped("job_stopped"),
        waiting_for_task("waiting_for_task");
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

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
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
