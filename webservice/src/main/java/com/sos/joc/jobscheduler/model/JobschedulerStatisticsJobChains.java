package com.sos.joc.jobscheduler.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "any", "stopped" })

public class JobschedulerStatisticsJobChains {

    @JsonProperty("any")
    private Integer any;

    @JsonProperty("any")
    public Integer getAny() {
        return any;
    }

    @JsonProperty("any")
    public void setAny(Integer any) {
        this.any = any;
    }

    public JobschedulerStatisticsJobChains withAny(Integer any) {
        this.any = any;
        return this;
    }

    @JsonProperty("stopped")
    private Integer stopped;

    @JsonProperty("stopped")
    public Integer getStopped() {
        return stopped;
    }

    @JsonProperty("stopped")
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    public JobschedulerStatisticsJobChains withStopped(Integer stopped) {
        this.stopped = stopped;
        return this;
    }

}
