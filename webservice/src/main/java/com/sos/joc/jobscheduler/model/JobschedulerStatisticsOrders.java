package com.sos.joc.jobscheduler.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "any", "clustered", "suspended" })

public class JobschedulerStatisticsOrders {

    @JsonProperty("any")
    private Integer any;
    @JsonProperty("clustered")
    private Integer clustered;
    @JsonProperty("suspended")
    private Integer suspended;
    @JsonProperty("pending")
    private Integer pending;
    @JsonProperty("setback")
    private Integer setback;
    @JsonProperty("queued")
    private Integer queued;
    @JsonProperty("running")
    private Integer running;
    @JsonProperty("blacklist")
    private Integer blacklist;

    @JsonProperty("any")
    public Integer getAny() {
        return any;
    }

    @JsonProperty("any")
    public void setAny(Integer any) {
        this.any = any;
    }

    public JobschedulerStatisticsOrders withAny(Integer any) {
        this.any = any;
        return this;
    }

    @JsonProperty("clustered")
    public Integer getClustered() {
        return clustered;
    }

    @JsonProperty("clustered")
    public void setClustered(Integer clustered) {
        this.clustered = clustered;
    }

    public JobschedulerStatisticsOrders withClustered(Integer clustered) {
        this.clustered = clustered;
        return this;
    }

    @JsonProperty("suspended")
    public Integer getSuspended() {
        return suspended;
    }

    @JsonProperty("suspended")
    public void setSuspended(Integer suspended) {
        this.suspended = suspended;
    }

    public JobschedulerStatisticsOrders withSuspended(Integer suspended) {
        this.suspended = suspended;
        return this;
    }

    @JsonProperty("pending")
    public Integer getPending() {
        return pending;
    }

    @JsonProperty("pending")
    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public JobschedulerStatisticsOrders withPending(Integer pending) {
        this.pending = pending;
        return this;
    }

    @JsonProperty("setback")
    public Integer getSetback() {
        return setback;
    }

    @JsonProperty("setback")
    public void setSetback(Integer setback) {
        this.setback = setback;
    }

    public JobschedulerStatisticsOrders withSetback(Integer setback) {
        this.setback = setback;
        return this;
    }

    @JsonProperty("queued")
    public Integer getQueued() {
        return queued;
    }

    @JsonProperty("queued")
    public void setQueued(Integer queued) {
        this.queued = queued;
    }

    public JobschedulerStatisticsOrders withQueued(Integer queued) {
        this.queued = queued;
        return this;
    }

 
    @JsonProperty("running")
    public Integer getRunning() {
        return running;
    }

    @JsonProperty("running")
    public void setRunning(Integer running) {
        this.running = running;
    }

    public JobschedulerStatisticsOrders withRunning(Integer running) {
        this.running = running;
        return this;
    }

    @JsonProperty("blacklist")
    public Integer getBlacklist() {
        return blacklist;
    }

    @JsonProperty("blacklist")
    public void setBlacklist(Integer blacklist) {
        this.blacklist = blacklist;
    }

    public JobschedulerStatisticsOrders withBlacklist(Integer blacklist) {
        this.blacklist = blacklist;
        return this;
    }

}
