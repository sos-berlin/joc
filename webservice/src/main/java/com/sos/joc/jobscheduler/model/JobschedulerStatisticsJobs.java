package com.sos.joc.jobscheduler.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "any", "need_process", "running", "stopped" })

public class JobschedulerStatisticsJobs {

    @JsonProperty("any")
    private Integer any;
    @JsonProperty("need_process")
    private Integer needProcess;
    @JsonProperty("running")
    private Integer running;
    @JsonProperty("stopped")
    private Integer stopped;

    @JsonProperty("any")
    public Integer getAny() {
        return any;
    }

    @JsonProperty("any")
    public void setAny(Integer any) {
        this.any = any;
    }

    public JobschedulerStatisticsJobs withAny(Integer any) {
        this.any = any;
        return this;
    }

    @JsonProperty("need_process")
    public Integer getNeedProcess() {
        return needProcess;
    }

    @JsonProperty("need_process")
    public void setNeedProcess(Integer needProcess) {
        this.needProcess = needProcess;
    }

    public JobschedulerStatisticsJobs withNeedProcess(Integer needProcess) {
        this.needProcess = needProcess;
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

    public JobschedulerStatisticsJobs withRunning(Integer running) {
        this.running = running;
        return this;
    }

    @JsonProperty("stopped")
    public Integer getStopped() {
        return stopped;
    }

    @JsonProperty("stopped")
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    public JobschedulerStatisticsJobs withStopped(Integer stopped) {
        this.stopped = stopped;
        return this;
    }

}
