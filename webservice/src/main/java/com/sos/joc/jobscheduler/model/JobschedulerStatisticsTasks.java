package com.sos.joc.jobscheduler.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "any", "running" })

public class JobschedulerStatisticsTasks {

    @JsonProperty("any")
    private Integer any;
    @JsonProperty("running")
    private Integer running;

    @JsonProperty("any")
    public Integer getAny() {
        return any;
    }

    @JsonProperty("any")
    public void setAny(Integer any) {
        this.any = any;
    }

    public JobschedulerStatisticsTasks withAny(Integer any) {
        this.any = any;
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

    public JobschedulerStatisticsTasks withRunning(Integer running) {
        this.running = running;
        return this;
    }

}
