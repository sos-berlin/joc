
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * clusterMember
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class UrlParamSchema {

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
     * 
     * @return
     *     The jobschedulerId
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(host).append(port).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UrlParamSchema) == false) {
            return false;
        }
        UrlParamSchema rhs = ((UrlParamSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(host, rhs.host).append(port, rhs.port).isEquals();
    }

}
