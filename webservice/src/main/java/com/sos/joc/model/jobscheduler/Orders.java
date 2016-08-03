
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * TODO here we need in addition: setback, waitingForResource, running, blacklist
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "any",
    "clustered",
    "pending",
    "suspended",
    "setback",
    "waitingForResource",
    "running",
    "blacklist"
})
public class Orders {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("any")
    private Integer any = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("clustered")
    private Integer clustered = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("pending")
    private Integer pending = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("suspended")
    private Integer suspended = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("setback")
    private Integer setback = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("waitingForResource")
    private Integer waitingForResource = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("running")
    private Integer running = 0;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("blacklist")
    private Integer blacklist = 0;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The any
     */
    @JsonProperty("any")
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
    @JsonProperty("any")
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
    @JsonProperty("clustered")
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
    @JsonProperty("clustered")
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
    @JsonProperty("pending")
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
    @JsonProperty("pending")
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
    @JsonProperty("suspended")
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
    @JsonProperty("suspended")
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
    @JsonProperty("setback")
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
    @JsonProperty("setback")
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
    @JsonProperty("waitingForResource")
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
    @JsonProperty("waitingForResource")
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
    @JsonProperty("running")
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
    @JsonProperty("running")
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
    @JsonProperty("blacklist")
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
    @JsonProperty("blacklist")
    public void setBlacklist(Integer blacklist) {
        this.blacklist = blacklist;
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
        return new HashCodeBuilder().append(any).append(clustered).append(pending).append(suspended).append(setback).append(waitingForResource).append(running).append(blacklist).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(any, rhs.any).append(clustered, rhs.clustered).append(pending, rhs.pending).append(suspended, rhs.suspended).append(setback, rhs.setback).append(waitingForResource, rhs.waitingForResource).append(running, rhs.running).append(blacklist, rhs.blacklist).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
