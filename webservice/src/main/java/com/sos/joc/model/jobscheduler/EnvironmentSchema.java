
package com.sos.joc.model.jobscheduler;

import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler environment
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class EnvironmentSchema {

    /**
     * 
     * (Required)
     * 
     */
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    private String host;
    /**
     * port
     * <p>
     * 
     * (Required)
     * 
     */
    private Integer port;
    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     */
    private Os os;
    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     */
    private State state;
    /**
     * jobscheduler cluster member type
     * <p>
     * 
     * 
     */
    private ClusterMemberTypeSchema clusterType;

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
     * (Required)
     * 
     * @return
     *     The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * (Required)
     * 
     * @param version
     *     The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The host
     */
    public String getHost() {
        return host;
    }

    /**
     * 
     * (Required)
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
     * (Required)
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
     * (Required)
     * 
     * @param port
     *     The port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The os
     */
    public Os getOs() {
        return os;
    }

    /**
     * jobscheduler platform
     * <p>
     * 
     * (Required)
     * 
     * @param os
     *     The os
     */
    public void setOs(Os os) {
        this.os = os;
    }

    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    public State getState() {
        return state;
    }

    /**
     * jobscheduler state
     * <p>
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * jobscheduler cluster member type
     * <p>
     * 
     * 
     * @return
     *     The clusterType
     */
    public ClusterMemberTypeSchema getClusterType() {
        return clusterType;
    }

    /**
     * jobscheduler cluster member type
     * <p>
     * 
     * 
     * @param clusterType
     *     The clusterType
     */
    public void setClusterType(ClusterMemberTypeSchema clusterType) {
        this.clusterType = clusterType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(version).append(host).append(port).append(os).append(state).append(clusterType).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof EnvironmentSchema) == false) {
            return false;
        }
        EnvironmentSchema rhs = ((EnvironmentSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(version, rhs.version).append(host, rhs.host).append(port, rhs.port).append(os, rhs.os).append(state, rhs.state).append(clusterType, rhs.clusterType).isEquals();
    }

}
