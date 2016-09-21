
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * clusterMemberTimeoutParam
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class UrlTimeoutParamSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    private String host;
    /**
     * port
     * <p>
     * 
     * 
     */
    private Integer port;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    private Integer timeout;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * 
     * @return
     *     The host
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * @param host
     *     The host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * port
     * <p>
     * 
     * 
     * @return
     *     The port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * port
     * <p>
     * 
     * 
     * @param port
     *     The port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param timeout
     *     The timeout
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(host).append(port).append(timeout).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UrlTimeoutParamSchema) == false) {
            return false;
        }
        UrlTimeoutParamSchema rhs = ((UrlTimeoutParamSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(host, rhs.host).append(port, rhs.port).append(timeout, rhs.timeout).isEquals();
    }

}
