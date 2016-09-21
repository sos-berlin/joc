
package com.sos.joc.model.order;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Orders {

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer pending;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer running;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer suspended;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer setback;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer waitingForResource;
    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer blacklist;

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
     * 
     * @param waitingForResource
     *     The waitingForResource
     */
    public void setWaitingForResource(Integer waitingForResource) {
        this.waitingForResource = waitingForResource;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The blacklist
     */
    public Integer getBlacklist() {
        return blacklist;
    }

    /**
     * non negative integer
     * <p>
     * 
     * (Required)
     * 
     * @param blacklist
     *     The blacklist
     */
    public void setBlacklist(Integer blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pending).append(running).append(suspended).append(setback).append(waitingForResource).append(blacklist).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Orders) == false) {
            return false;
        }
        Orders rhs = ((Orders) other);
        return new EqualsBuilder().append(pending, rhs.pending).append(running, rhs.running).append(suspended, rhs.suspended).append(setback, rhs.setback).append(waitingForResource, rhs.waitingForResource).append(blacklist, rhs.blacklist).isEquals();
    }

}
