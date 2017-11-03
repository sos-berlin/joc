
package com.sos.joc.model.job;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job summary
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "pending",
    "running",
    "stopped",
    "waitingForProcess",
    "tasks"
})
public class JobsSummary {

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
    @JsonProperty("stopped")
    private Integer stopped;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("waitingForProcess")
    private Integer waitingForProcess;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    private Integer tasks;

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
     *     The stopped
     */
    @JsonProperty("stopped")
    public Integer getStopped() {
        return stopped;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param stopped
     *     The stopped
     */
    @JsonProperty("stopped")
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The waitingForProcess
     */
    @JsonProperty("waitingForProcess")
    public Integer getWaitingForProcess() {
        return waitingForProcess;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param waitingForProcess
     *     The waitingForProcess
     */
    @JsonProperty("waitingForProcess")
    public void setWaitingForProcess(Integer waitingForProcess) {
        this.waitingForProcess = waitingForProcess;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The tasks
     */
    @JsonProperty("tasks")
    public Integer getTasks() {
        return tasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param tasks
     *     The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(Integer tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pending).append(running).append(stopped).append(waitingForProcess).append(tasks).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobsSummary) == false) {
            return false;
        }
        JobsSummary rhs = ((JobsSummary) other);
        return new EqualsBuilder().append(pending, rhs.pending).append(running, rhs.running).append(stopped, rhs.stopped).append(waitingForProcess, rhs.waitingForProcess).append(tasks, rhs.tasks).isEquals();
    }

}
