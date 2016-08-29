
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
 * jobChainNode (permanent part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "nextNode",
    "errorNode",
    "job",
    "jobChain",
    "level",
    "onError",
    "delay"
})
public class Node {

    @JsonProperty("name")
    private String name;
    @JsonProperty("nextNode")
    private String nextNode;
    @JsonProperty("errorNode")
    private String errorNode;
    /**
     * job object (permanent part)
     * <p>
     * 
     * 
     */
    @JsonProperty("job")
    private Job job;
    /**
     * nested job chain (permanent part)
     * <p>
     * 
     * 
     */
    @JsonProperty("jobChain")
    private JobChain_ jobChain;
    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("onError")
    private Node.OnError onError;
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
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
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
     * @return
     *     The nextNode
     */
    @JsonProperty("nextNode")
    public String getNextNode() {
        return nextNode;
    }

    /**
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
     * @return
     *     The errorNode
     */
    @JsonProperty("errorNode")
    public String getErrorNode() {
        return errorNode;
    }

    /**
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
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * nested job chain (permanent part)
     * <p>
     * 
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public JobChain_ getJobChain() {
        return jobChain;
    }

    /**
     * nested job chain (permanent part)
     * <p>
     * 
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(JobChain_ jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     * @return
     *     The level
     */
    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     * @param level
     *     The level
     */
    @JsonProperty("level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 
     * @return
     *     The onError
     */
    @JsonProperty("onError")
    public Node.OnError getOnError() {
        return onError;
    }

    /**
     * 
     * @param onError
     *     The onError
     */
    @JsonProperty("onError")
    public void setOnError(Node.OnError onError) {
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
        return new HashCodeBuilder().append(name).append(nextNode).append(errorNode).append(job).append(jobChain).append(level).append(onError).append(delay).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Node) == false) {
            return false;
        }
        Node rhs = ((Node) other);
        return new EqualsBuilder().append(name, rhs.name).append(nextNode, rhs.nextNode).append(errorNode, rhs.errorNode).append(job, rhs.job).append(jobChain, rhs.jobChain).append(level, rhs.level).append(onError, rhs.onError).append(delay, rhs.delay).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum OnError {

        __EMPTY__(""),
        suspend("suspend"),
        setback("setback");
        private final String value;
        private final static Map<String, Node.OnError> CONSTANTS = new HashMap<String, Node.OnError>();

        static {
            for (Node.OnError c: values()) {
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
        public static Node.OnError fromValue(String value) {
            Node.OnError constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
