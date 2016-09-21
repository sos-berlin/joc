
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * TODO here we need in addition: setback, waitingForResource, running, blacklist
 * 
 */
@Generated("org.jsonschema2pojo")
public class Orders {

    /**
     * 
     * (Required)
     * 
     */
    private Integer any = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer clustered = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer pending = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer suspended = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer setback = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer waitingForResource = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer running = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer blacklist = 0;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The any
     */
    public Integer getAny() {
        return any;
    }

    /**
     * 
     * (Required)
     * 
     * @param any
     *     The any
     */
    public void setAny(Integer any) {
        this.any = any;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The clustered
     */
    public Integer getClustered() {
        return clustered;
    }

    /**
     * 
     * (Required)
     * 
     * @param clustered
     *     The clustered
     */
    public void setClustered(Integer clustered) {
        this.clustered = clustered;
    }

    /**
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
        return new HashCodeBuilder().append(any).append(clustered).append(pending).append(suspended).append(setback).append(waitingForResource).append(running).append(blacklist).toHashCode();
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
        return new EqualsBuilder().append(any, rhs.any).append(clustered, rhs.clustered).append(pending, rhs.pending).append(suspended, rhs.suspended).append(setback, rhs.setback).append(waitingForResource, rhs.waitingForResource).append(running, rhs.running).append(blacklist, rhs.blacklist).isEquals();
    }

}
