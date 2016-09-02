
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
public class Node {

    private String name;
    private String nextNode;
    private String errorNode;
    /**
     * job object (permanent part)
     * <p>
     * 
     * 
     */
    private Job job;
    /**
     * nested job chain (permanent part)
     * <p>
     * 
     * 
     */
    private JobChain_ jobChain;
    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    private Integer level;
    /**
     * possible values are 'suspend', 'setback' or it isn't set
     * 
     */
    private String onError;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer delay;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The nextNode
     */
    public String getNextNode() {
        return nextNode;
    }

    /**
     * 
     * @param nextNode
     *     The nextNode
     */
    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * 
     * @return
     *     The errorNode
     */
    public String getErrorNode() {
        return errorNode;
    }

    /**
     * 
     * @param errorNode
     *     The errorNode
     */
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
    public void setJobChain(JobChain_ jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     * @return
     *     The level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     * @param level
     *     The level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * possible values are 'suspend', 'setback' or it isn't set
     * 
     * @return
     *     The onError
     */
    public String getOnError() {
        return onError;
    }

    /**
     * possible values are 'suspend', 'setback' or it isn't set
     * 
     * @param onError
     *     The onError
     */
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
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(nextNode).append(errorNode).append(job).append(jobChain).append(level).append(onError).append(delay).toHashCode();
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
        return new EqualsBuilder().append(name, rhs.name).append(nextNode, rhs.nextNode).append(errorNode, rhs.errorNode).append(job, rhs.job).append(jobChain, rhs.jobChain).append(level, rhs.level).append(onError, rhs.onError).append(delay, rhs.delay).isEquals();
    }

}
