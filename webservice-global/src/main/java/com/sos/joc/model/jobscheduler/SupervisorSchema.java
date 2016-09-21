
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler supervisor
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class SupervisorSchema {

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
     * 
     */
    private Os os;
    /**
     * 
     * (Required)
     * 
     */
    private SupervisorSchema.State state;

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
     * 
     * @param os
     *     The os
     */
    public void setOs(Os os) {
        this.os = os;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    public SupervisorSchema.State getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    public void setState(SupervisorSchema.State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(version).append(host).append(port).append(os).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SupervisorSchema) == false) {
            return false;
        }
        SupervisorSchema rhs = ((SupervisorSchema) other);
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(version, rhs.version).append(host, rhs.host).append(port, rhs.port).append(os, rhs.os).append(state, rhs.state).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum State {

        UNKNOWN("UNKNOWN"),
        RUNNING("RUNNING"),
        PAUSED("PAUSED"),
        WAITING_FOR_ACTIVATION("WAITING_FOR_ACTIVATION"),
        WAITING_FOR_DATABASE("WAITING_FOR_DATABASE");
        private final String value;
        private final static Map<String, SupervisorSchema.State> CONSTANTS = new HashMap<String, SupervisorSchema.State>();

        static {
            for (SupervisorSchema.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static SupervisorSchema.State fromValue(String value) {
            SupervisorSchema.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
