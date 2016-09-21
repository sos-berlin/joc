
package com.sos.joc.model.job;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job order summary
 * <p>
 * only relevant for order jobs and is empty if job's order queue is empty
 * 
 */
@Generated("org.jsonschema2pojo")
public class OrdersSummary {

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer pending;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer running;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer suspended;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer setback;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer waitingForResource;

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The pending
     */
    public Integer getPending() {
        return pending;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param pending
     *     The pending
     */
    public void setPending(Integer pending) {
        this.pending = pending;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The running
     */
    public Integer getRunning() {
        return running;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param running
     *     The running
     */
    public void setRunning(Integer running) {
        this.running = running;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The suspended
     */
    public Integer getSuspended() {
        return suspended;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param suspended
     *     The suspended
     */
    public void setSuspended(Integer suspended) {
        this.suspended = suspended;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The setback
     */
    public Integer getSetback() {
        return setback;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param setback
     *     The setback
     */
    public void setSetback(Integer setback) {
        this.setback = setback;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The waitingForResource
     */
    public Integer getWaitingForResource() {
        return waitingForResource;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param waitingForResource
     *     The waitingForResource
     */
    public void setWaitingForResource(Integer waitingForResource) {
        this.waitingForResource = waitingForResource;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pending).append(running).append(suspended).append(setback).append(waitingForResource).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrdersSummary) == false) {
            return false;
        }
        OrdersSummary rhs = ((OrdersSummary) other);
        return new EqualsBuilder().append(pending, rhs.pending).append(running, rhs.running).append(suspended, rhs.suspended).append(setback, rhs.setback).append(waitingForResource, rhs.waitingForResource).isEquals();
    }

}
