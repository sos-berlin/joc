
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
    "delay",
    "remove",
    "move"
})
public class JobChainNodeP {

    @JsonProperty("name")
    private String name;
    @JsonProperty("nextNode")
    private String nextNode;
    @JsonProperty("errorNode")
    private String errorNode;
    /**
     * 
     */
    @JsonProperty("job")
    private JobChainNodeJobP job;
    /**
     * job chain object is included in nestedJobChains collection
     * 
     */
    @JsonProperty("jobChain")
    private JobChainNodeJobChainP jobChain;
    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    @JsonProperty("level")
    private Integer level;
    /**
     * possible values are 'suspend', 'setback' or it isn't set
     * 
     */
    @JsonProperty("onError")
    private String onError;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("delay")
    private Integer delay;
    /**
     * for file order sink
     * 
     */
    @JsonProperty("remove")
    private Boolean remove;
    /**
     * for file order sink, a directory path is expected
     * 
     */
    @JsonProperty("move")
    private String move;

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
     * 
     * @return
     *     The job
     */
    @JsonProperty("job")
    public JobChainNodeJobP getJob() {
        return job;
    }

    /**
     * 
     * @param job
     *     The job
     */
    @JsonProperty("job")
    public void setJob(JobChainNodeJobP job) {
        this.job = job;
    }

    /**
     * job chain object is included in nestedJobChains collection
     * 
     * @return
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public JobChainNodeJobChainP getJobChain() {
        return jobChain;
    }

    /**
     * job chain object is included in nestedJobChains collection
     * 
     * @param jobChain
     *     The jobChain
     */
    @JsonProperty("jobChain")
    public void setJobChain(JobChainNodeJobChainP jobChain) {
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
     * possible values are 'suspend', 'setback' or it isn't set
     * 
     * @return
     *     The onError
     */
    @JsonProperty("onError")
    public String getOnError() {
        return onError;
    }

    /**
     * possible values are 'suspend', 'setback' or it isn't set
     * 
     * @param onError
     *     The onError
     */
    @JsonProperty("onError")
    public void setOnError(String onError) {
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

    /**
     * for file order sink
     * 
     * @return
     *     The remove
     */
    @JsonProperty("remove")
    public Boolean getRemove() {
        return remove;
    }

    /**
     * for file order sink
     * 
     * @param remove
     *     The remove
     */
    @JsonProperty("remove")
    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    /**
     * for file order sink, a directory path is expected
     * 
     * @return
     *     The move
     */
    @JsonProperty("move")
    public String getMove() {
        return move;
    }

    /**
     * for file order sink, a directory path is expected
     * 
     * @param move
     *     The move
     */
    @JsonProperty("move")
    public void setMove(String move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(nextNode).append(errorNode).append(job).append(jobChain).append(level).append(onError).append(delay).append(remove).append(move).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainNodeP) == false) {
            return false;
        }
        JobChainNodeP rhs = ((JobChainNodeP) other);
        return new EqualsBuilder().append(name, rhs.name).append(nextNode, rhs.nextNode).append(errorNode, rhs.errorNode).append(job, rhs.job).append(jobChain, rhs.jobChain).append(level, rhs.level).append(onError, rhs.onError).append(delay, rhs.delay).append(remove, rhs.remove).append(move, rhs.move).isEquals();
    }

}
