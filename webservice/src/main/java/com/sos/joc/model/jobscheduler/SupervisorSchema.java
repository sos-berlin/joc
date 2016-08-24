
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobscheduler supervisor
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "version",
    "host",
    "port",
    "os",
    "state"
})
public class SupervisorSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("version")
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("host")
    private String host;
    /**
     * port
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("port")
    private Integer port;
    /**
     * jobscheduler platform
     * <p>
     * 
     * 
     */
    @JsonProperty("os")
    private Os os;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    private SupervisorSchema.State state;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
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
    @JsonProperty("jobschedulerId")
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
    @JsonProperty("version")
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
    @JsonProperty("version")
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
    @JsonProperty("host")
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
    @JsonProperty("host")
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
    @JsonProperty("port")
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
    @JsonProperty("port")
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
    @JsonProperty("os")
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
    @JsonProperty("os")
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
    @JsonProperty("state")
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
    @JsonProperty("state")
    public void setState(SupervisorSchema.State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(version).append(host).append(port).append(os).append(state).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(version, rhs.version).append(host, rhs.host).append(port, rhs.port).append(os, rhs.os).append(state, rhs.state).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum State {

        unknown("unknown"),
        running("running"),
        paused("paused"),
        waiting_for_activation("waiting_for_activation"),
        waiting_for_database("waiting_for_database");
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

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
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
