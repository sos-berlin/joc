
package com.sos.joc.model.jobChain;

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
import com.sos.joc.model.job.Job;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * JobNode (permanent part)
 * <p>
 * job chain node object with assigned a job
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "nextNode",
    "errorNode",
    "job",
    "onError",
    "delay"
})
public class Node_ {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("nextNode")
    private String nextNode;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorNode")
    private String errorNode;
    /**
     * job object (permanent part)
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("job")
    private Job job;
    @JsonProperty("onError")
    private Node_.OnError onError;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    private Integer delay;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The nextNode
     */
    @JsonProperty("nextNode")
    public String getNextNode() {
        return nextNode;
    }

    /**
     * 
     * (Required)
     * 
     * @param nextNode
     *     The nextNode
     */
    @JsonProperty("nextNode")
    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The errorNode
     */
    @JsonProperty("errorNode")
    public String getErrorNode() {
        return errorNode;
    }

    /**
     * 
     * (Required)
     * 
     * @param errorNode
     *     The errorNode
     */
    @JsonProperty("errorNode")
    public void setErrorNode(String errorNode) {
        this.errorNode = errorNode;
    }

    /**
     * job object (permanent part)
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public Job getJob() {
        return job;
    }

    /**
     * job object (permanent part)
     * <p>
     * 
     * (Required)
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The onError
     */
    @JsonProperty("onError")
    public Node_.OnError getOnError() {
        return onError;
    }

    /**
     * 
     * @param onError
     *     The onError
     */
    @JsonProperty("onError")
    public void setOnError(Node_.OnError onError) {
        this.onError = onError;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The delay
     */
    @JsonProperty("delay")
    public Integer getDelay() {
        return delay;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param delay
     *     The delay
     */
    @JsonProperty("delay")
    public void setDelay(Integer delay) {
        this.delay = delay;
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
        return new HashCodeBuilder().append(name).append(nextNode).append(errorNode).append(job).append(onError).append(delay).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Node_) == false) {
            return false;
        }
        Node_ rhs = ((Node_) other);
        return new EqualsBuilder().append(name, rhs.name).append(nextNode, rhs.nextNode).append(errorNode, rhs.errorNode).append(job, rhs.job).append(onError, rhs.onError).append(delay, rhs.delay).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum OnError {

        __EMPTY__(""),
        suspend("suspend"),
        setback("setback");
        private final String value;
        private final static Map<String, Node_.OnError> CONSTANTS = new HashMap<String, Node_.OnError>();

        static {
            for (Node_.OnError c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private OnError(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Node_.OnError fromValue(String value) {
            Node_.OnError constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
