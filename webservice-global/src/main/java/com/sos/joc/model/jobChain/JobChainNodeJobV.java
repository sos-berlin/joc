
package com.sos.joc.model.jobChain;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.job.JobState;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "path",
    "state",
    "configurationStatus"
})
public class JobChainNodeJobV {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * job state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    private JobState state;
    /**
     * configuration status
     * <p>
     * 
     * 
     */
    @JsonProperty("configurationStatus")
    private ConfigurationState configurationStatus;

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * job state
     * <p>
     * 
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public JobState getState() {
        return state;
    }

    /**
     * job state
     * <p>
     * 
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(JobState state) {
        this.state = state;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @return
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public ConfigurationState getConfigurationStatus() {
        return configurationStatus;
    }

    /**
     * configuration status
     * <p>
     * 
     * 
     * @param configurationStatus
     *     The configurationStatus
     */
    @JsonProperty("configurationStatus")
    public void setConfigurationStatus(ConfigurationState configurationStatus) {
        this.configurationStatus = configurationStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(state).append(configurationStatus).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainNodeJobV) == false) {
            return false;
        }
        JobChainNodeJobV rhs = ((JobChainNodeJobV) other);
        return new EqualsBuilder().append(path, rhs.path).append(state, rhs.state).append(configurationStatus, rhs.configurationStatus).isEquals();
    }

}
