
package com.sos.joc.model.order;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chain order summary
 * <p>
 * only relevant for order jobs and is empty if job's order queue is empty
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "pending",
    "running",
    "suspended",
    "setback",
    "waitingForResource",
    "blacklist"
})
public class OrdersSummary {

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("pending")
    private Integer pending;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("running")
    private Integer running;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("suspended")
    private Integer suspended;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("setback")
    private Integer setback;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("waitingForResource")
    private Integer waitingForResource;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("blacklist")
    private Integer blacklist;

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The pending
     */
    @JsonProperty("pending")
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
    @JsonProperty("pending")
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
    @JsonProperty("running")
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
    @JsonProperty("running")
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
    @JsonProperty("suspended")
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
    @JsonProperty("suspended")
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
    @JsonProperty("setback")
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
    @JsonProperty("setback")
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
    @JsonProperty("waitingForResource")
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
    @JsonProperty("waitingForResource")
    public void setWaitingForResource(Integer waitingForResource) {
        this.waitingForResource = waitingForResource;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The blacklist
     */
    @JsonProperty("blacklist")
    public Integer getBlacklist() {
        return blacklist;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param blacklist
     *     The blacklist
     */
    @JsonProperty("blacklist")
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
        if ((other instanceof OrdersSummary) == false) {
            return false;
        }
        OrdersSummary rhs = ((OrdersSummary) other);
        return new EqualsBuilder().append(pending, rhs.pending).append(running, rhs.running).append(suspended, rhs.suspended).append(setback, rhs.setback).append(waitingForResource, rhs.waitingForResource).append(blacklist, rhs.blacklist).isEquals();
    }

}
