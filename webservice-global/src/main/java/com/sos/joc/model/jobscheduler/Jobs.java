
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Jobs {

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
    private Integer running = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer stopped = 0;
    /**
     * 
     * (Required)
     * 
     */
    private Integer needProcess = 0;

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
     *     The stopped
     */
    public Integer getStopped() {
        return stopped;
    }

    /**
     * 
     * (Required)
     * 
     * @param stopped
     *     The stopped
     */
    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The needProcess
     */
    public Integer getNeedProcess() {
        return needProcess;
    }

    /**
     * 
     * (Required)
     * 
     * @param needProcess
     *     The needProcess
     */
    public void setNeedProcess(Integer needProcess) {
        this.needProcess = needProcess;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(any).append(running).append(stopped).append(needProcess).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Jobs) == false) {
            return false;
        }
        Jobs rhs = ((Jobs) other);
        return new EqualsBuilder().append(any, rhs.any).append(running, rhs.running).append(stopped, rhs.stopped).append(needProcess, rhs.needProcess).isEquals();
    }

}
