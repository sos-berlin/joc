
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
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
@Generated("org.jsonschema2pojo")
public class Node_ {

    /**
     * 
     * (Required)
     * 
     */
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    private String nextNode;
    /**
     * 
     * (Required)
     * 
     */
    private String errorNode;
    /**
     * job object (permanent part)
     * <p>
     * 
     * (Required)
     * 
     */
    private Job job;
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
     * (Required)
     * 
     * @return
     *     The name
     */
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
    public void setJob(Job job) {
        this.job = job;
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
        return new HashCodeBuilder().append(name).append(nextNode).append(errorNode).append(job).append(onError).append(delay).toHashCode();
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
        return new EqualsBuilder().append(name, rhs.name).append(nextNode, rhs.nextNode).append(errorNode, rhs.errorNode).append(job, rhs.job).append(onError, rhs.onError).append(delay, rhs.delay).isEquals();
    }

}
